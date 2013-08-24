[![Build Status](https://travis-ci.org/davidcarboni/Webulizor.png?branch=master)](https://travis-ci.org/davidcarboni/Webulizor)
Webulizor
----------


### What is it?

Webulizor is a Java webapp micro-framework. It's built on a couple of ideas:
 * in a world of continuous delivery the best place to put most configuration is directly in the code. If you need to change configuration it's safer to push a release than edit files on a live system.
 * refactoring code shouldn't break links. Why should you have to manually keep things in synch across code, configuration and HTML?
 * finally, `/servlet/` in your URLs is visual noise and just plain ugly. Webulizor lets you put your controllers at the top level whilst forwarding anything that has a file extension to the default servlet (.css, .js, .png, etc.) 

Webulizor provides you with a couple of helpful bootstrap defaults:
 * HSQLDB so you can get on with prototyping instead of installing a database
 * Apache Velocity for response rendering
 
Defaults can be overridden or ignored and you're free to configure your own front-controller servlet receive requests dispatched by the Webulizor filter, just copy and tweak the `web.xml` included with Webulizor. 

### The basics

Webulizor assumes that URLs with a file extension are static content and URLs without a file extension are controllers. It uses a servlet filter to dispatch requests to either the default servlet or the application servlet. Using a filter means there's no `/servlet/` in your URLs, so everything can be top-level.

Controllers are defined using annotations such as `@Route` and `@HomeAction`. `@Route` allows you to specify the URI for a controller and `@HomeAction` indicates the default controller. A few base classes are provided to get you started: `AbstractAction`, `RedirectAction` and `ViewAction`. By default you get Apache Velocity for templating. `ViewAction` defaults to looking for a `.html` resource in the same package structure as your controller, or you're free to define a different resource path. The default database is HSQLDB, which can be changed. Controllers that use a database connection are transactional by default but can be marked as non-transactional where necessary.


### Getting started

The quickest way to get started is:
 * add Webulizor to your project as a WAR overlay (`<type>war</type>`)
 * reference the `-classes` JAR in your dependencies (`<classifier>classes</classifier>`)
 * create a class that extends `ViewAction` and annotate it with `@Route` and `@HomeAction`
 * create a Velocity template to match your package structure and the name of your class with a `.html` extension.


### Example pom configuration

Here are some `pom.xml` snippets to get you started:

 * Webulizor artifact details:
  
 ```
<properties>
    <webulizor.groupid>com.github.davidcarboni</webulizor.groupid>
    <webulizor.artifactid>webulizor</webulizor.artifactid>
    <webulizor.version>0.6.5</webulizor.version>
</properties>
```

 * Webulizor dependencies:

```
 <dependencies>

    <!-- Webulizor: -->
    <dependency>
        <groupId>${webulizor.groupid}</groupId>
        <artifactId>${webulizor.artifactid}</artifactId>
        <type>war</type>
        <version>${webulizor.version}</version>
    </dependency>
    <dependency>
        <groupId>${webulizor.groupid}</groupId>
        <artifactId>${webulizor.artifactid}</artifactId>
        <version>${webulizor.version}</version>
        <classifier>classes</classifier>
    </dependency>

    <!-- You'll probably want the Servlet API: -->
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>servlet-api</artifactId>
        <version>2.5</version>
        <scope>provided</scope>
    </dependency>
    <!-- or -->
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>javax.servlet-api</artifactId>
        <version>3.1.0</version>
    </dependency>
        
 </dependencies>
```

 * WAR overlay configuration:
 
 ```
 <build>
    <finalName>mywebapp</finalName>

    <plugins>

        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-war-plugin</artifactId>
            <version>2.3</version>
            <configuration>

                <!-- If you're inheriting the webulizor web.xml you won't need to define your own: -->
                <failOnMissingWebXml>false</failOnMissingWebXml>

                <overlays>
                    <overlay>
                        <groupId>${webulizor.groupid}</groupId>
                        <artifactId>${webulizor.artifactid}</artifactId>
                    </overlay>
                </overlays>

            </configuration>
        </plugin>

    </plugins>

 </build>
```
