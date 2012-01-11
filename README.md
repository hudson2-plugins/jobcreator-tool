Overview
---------
The purpose of this program is to allow Hudson administrators to script the creation of jobs. It does this by using 
FreeMarker (http://freemarker.sourceforge.net/) templates combining those with the job definitions 
found in a "pipeline" file and outputing config.xml files which can be read by Hudson.

The pipeline file contains two sets of entries namely "jobs" and "groups". 

Jobs specify a template, properties and other metadata. Jobs can inherit from each other allowing for 
usecases like specifying a generic builder job from that inherit project specific build jobs. The project specific 
build jobs needs then only specify scm location and polling.

Group definea group of jobs, and can also hold properties. One use case for specifying properties in a group is to 
have the groups match environments you like to deploy code in. It is very likely then dev environment deployment jobs
need to run more often than a preprod job, and you could solve this by specifying diffent values for the timer trigger 
in different groups.Another usecase could be that the "deploy service stubs" job should only be included in the development 
in environment (group). Groups can also be inherited allowing to specify common defaults.

The freemarker templates can be written as the user see fit, as the only thing the creator tool does is resolve the 
properties defined in the pipeline file and provide those as data to the template. After that control is given 
to the freemarker engine to provide the finished xml.

Basic usage
-----------

The program is a standard java program which can be run using Java 6. The jar file downloaded is a shaded jar meaning 
that the 3. party dependencies have been packed into the jar file. 

When running the program the following input parameters are needed

*   --input (-i) <pipeline file>: The pipeline file to use 
*   --template <template dir>: The directory where all templates are stored. paths to templates are 
    evaluated from here.
*   --output (-o) <output directory>: All finished files will be written here
*   --group (-g) <name>: The group of jobs to create config files for.

Once the program is started it does the following steps:

1.  Load the pipeline file
2.  Build the effective group definition by resolving any inheritance
3.  For each job included build the effective jobs based on the inheritance
4.  For each job apply any global properties specified in the group from step 2
5.  For each job apply any job specific properties specified in the group from step 2
6.  For each job build datamodel from properties, call freemarker and save the xml

The output is placed in the output directory and follows the format ${output.basedir}/${resolved.jobname}/config.xml. 
This format allows you to specify a hudson jbos folder (${HUDSON_HOME}/jobs/) directly and hudson will pick up new jobs 
when it reloads the configuration.

Integration with Hudson
------------------------


Defining pipelines
------------------
Pipeline definitions are written in XML (more formats comming later)

**Overall structure**

```xml 
<?xml version="1.0" encoding="UTF-8"?>
<ns1:pipeline xmlns:ns1='hudsonci.jobcreator.v1'>
  <name>example</name>
  <groups>
    <group>
      ...group definition...
    </group>
  </groups>
  <jobs>
    <job>
      ...job definition...
    </job>
  </jobs>  
</ns1:pipeline>
```
*  name: name of the pipeline, can be used as part of the resolved job name

**Job definition**

```xml 
<job name="example-jooname" template="example-template.ftl">
  <inherit>
    <job>example-other-job</job>
    <job>example-other-job</job>
  </inherit>    
  <downstream>
    <job>example-other-job</job>
  </downstream>
  <propertyset>
    <property propagation="..." merge="..." name="example-property-name">example-property-value</property>
  </propertyset>
</job>
```

*  name: The name of the job
*  template (Optional): The template to use, if not specified an inherited value is expected
*  inherit (Optional): Contains a list list of jobs to inherit values from processed in the order given
*  downstream (Optional): List of jobs which are downstream from this job (inside hudson job flow). this value is 
   available to the template. See "Advanced usage" for when to use
*  Propertyset (Optional): list of properties defined for this job
*  Propagation (Optional): how the property propergates to upstream/downstream jobs (See "Advanced usage")
*  Merge (Optional): How properties are merged during propagation (See "Advanced usage")

**Group definition**

```xml 
<group name="example-name" pattern="${pipeline}_${group}_${job}">
  <inherit>
    <group>example-other-group</group> 
  </inherit>
  <include>
    <job>example-jobname</job>
  </include> 
  <exclude>
    <job>example-jobname</job>
  </exclude>
  <propertyset job="job2">
    <property propagation="..." merge="..." name="example-property-name">example-property-value</property>
  </propertyset> 
  <propertyset>
    <property propagation="..." merge="..." name="example-property-name">example-property-value</property>
  </propertyset> 
 </group>

```

*  name: The name of the group
*  pattern (Optional): The pattern to use for resolved job names.The 3 tokens shown are replaced with the name of the 
   pipeline, active group and active job respectivaly. if not specified an inherited value is expected
*  inherit (Optional): Contains a list list of groups to inherit values from processed in the order given
*  Include (Optional): List of jobs to include
*  Exclude (Optional): List of jobs to exclude.The exlude takes precedence over include.However the handling of 
   include/eclude happen on every step in the inheritance chain so if a parent has excluded a job a child can re-include it.
*  Propertyset (Optional): list of properties defined for this group. If job attribute is not present properties are applied 
   to all jobs, otherwise only to the job specified.
*  Propagation (Optional): how the property propergates to upstream/downstream jobs (See "Advanced usage")
*  Merge (Optional): How properties are merged during propagation (See "Advanced usage")


Using FreeMarker
----------------

How you name the properties has an effect on how the datamodel is presented to the template. To put it simply 
the property is split into element with every "." (dot character). Parent elements are added as hashes (java.util.Map) 
and the leaf element is added as a string (java.lang.String). 

This split does pose a restriction since a element cannot both be a String and an container i.e. the having both "git.repo" 
and "git.repo.branch" is ilegal as repo would be both. On the other hand it helps in other situations e.g. image the these 
properties are defined "scm.git.repo" + "scm.git.branch", now we can do <#if scm.git??>.... print git segnment </#if> or
we can do other collection handling 

When using import directive remember that the path is evaluated from the template root dir, not the location of the 
current template.

The tool also creates some special properties

*   import.pipeline.name: Name of the pipeline as per pipeline xml
*   import.group.name: Name of the group being imported
*   import.jobs: comma separated list of all the jobs being imported
*   import.job.name: Name of job as per pipeline definition
*   import.job.resolvedname: Full name of the job as per the pattern defined for the group.
*   import.job.upstream: comma separated list of jobs which are upstream from the current job based on the downstream
    definition in the pipeline xml. Note only jobs being imported are included.
*   import.job.downstream: comma separated list of jobs which are upstream from the current job based on the downstream
    definition in the pipeline xml. Note only jobs being imported are included.
*   import.time: Data and time in human readable format of when the import was run

Advanced topics: Upstream/Downstream jobs
-----------------------------------------
The downstream notation on a job specification can be used to dynamically create a list of downstream/upstream jobs
based on the active set of jobs being imported. A classical example is a deploy step in different environments. Assume 
the following set of hudson jobs

* Deploy trigger job (no-op job used to start the actual deploy jobs in sync and concurrently)
* Deploy frontend
* Deploy backend
* Deploy stubs (Stubbing tool to stub away 3.party runtime dependencies to other systems)
* test trigger job (called by join plugin from deploy trigger job)

In most environments you only want the first 3 jobs, but in development you want them all. So you define a group for prod 
like configuration and one for dev configuration. In the template for the deploy trigger job there is a xml fragment for 
triggering the downstream jobs, and in the test trigger job there is a segment for copying deploy result from the 
individual deploy jobs.

If we don't use the downstream functionality, then we would need to define a property and manually ensure that it has
the right list of jobs in each environment and there would be no consistency check by the tool.

If we use the downstream job notation where deploy trigger list all 3 deploy jobs, and each of the deploy jobs list the 
test trigger jobs, the template would be able to use the properties import.job.upstream + import.job.downstream knowing
it only contained the jobs loaded this environment (group).

*Note:* The downstream notation has no impact on which jobs are loaded or any affect on the templates except from where 
they reference the special properties.

Advanced topics: Properties propagation
----------------------------------------

Progagation of properties is a way to specify a property on a jbo but also have it apply for upstream or downstream jobs.
Assuming the following job chain A -> B -> C -> D and we have a job parameter for specifying full or partial deployment.
In this case we could specify the following properties in the pipeline specification

*   On the "D" job : <property name="deployment.type">partial</property>
*   On the "C" job : <property propagation="upstream" name="deployment.type">full</property>

The propagation attribute can take the following values

* none: indicate that if this property has been defined with a upstream/downstream propagation in another job, the 
  propagation will stop at this job.
* continue (default): indicate that if this property has been defined with a upstream/downstream propagation 
  in another job, the  propagation will continue.
* upstream: indicate that this property should be propagated to all upstream jobs
* downstream: indicate that this property should be propagated to all downstream jobs

the merging attribute is used when handling the "conflict" of propagation seeing the same property. It can
take the following values:

* skip: Skip this job and don't use the propergated value.
* replace (default): Use the propagated value instead of the specified value
* append: append the propagated value to the end of the specified value with comma as a separator.
* prefix: prepend the propagated value to the beginning of the specified value with comma as a separator.
* list: build a comma separated list

The best way to illustrate how this conflict handling is done is by example. Continuing from the previous example, let us
assume that the propery is also specified for the B job, thus giving

*   On the "D" job : <property name="deployment.type">partial</property>
*   On the "C" job : <property propagation="upstream" name="deployment.type">full</property>
*   On the "B" job : <property propagation="????" merge="????" name="deployment.type">partial</property> 

First thing to do is look at how the value of the propagation attribute on job B changes things.

* upstream or downstream: The current propagation from "C" job is stopped before any merging is done
* none: the propagation from "C" is stopped but property is merged according to the merge value
* continue: the propagation from "C" will continue to job "A" and the property is merged according to the merge value

Secondly the merge value does changes the effective value of "deployment.type" on job be to:

* skip: partial (the specified value on job B is not touced) 
* replace: full (the specified value on job B is replaced)
* append: partial,full (appends the propagated value)
* prefix: full,partial (prepends the propagated value)

*Note* if there is multiple paths from one job to another e.g. via a diamond shaped job graph, no gurantees are made with
regards to the order.

*Note* Currently downstream propagated properties are evaluated before upstream, but that is a implementation detail,
which you should not rely on

Real world examples
--------------------


