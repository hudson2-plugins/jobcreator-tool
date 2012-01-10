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

Defining pipelines
------------------



Using FreeMarker
----------------


