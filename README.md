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


