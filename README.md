Name
====

Json Validation Service for WI2017

Description
===========

A Finch-based microservice that store, get and validate a simple JSON documts based on the schemas.

Building
========

To build, run the following SBT command:

    $ sbt dist

Configuration
=============

* Install H2 database
* Specify storage in /main/resources/application.conf (h2loc1)

Running
=======

To run:

    $ sbt "runMain Main"
