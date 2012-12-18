This Java-Webapp is a prototype for collecting arguments and facts for controversial discussed topics. It is currently deployed on http://argufactum.de

Technology stack:
- Spring, Spring MVC, Spring Security, Spring Social
- Hibernate ORM & Ehcache
- Atmosphere
- jQuery + timeago, tmpl & jQuery UI, Bootstrap

# Getting started
[Maven](http://maven.apache.org): you should have installed maven3 to be able to build the webapp.

You need to create 3 configuration files before you can start the application with the maven goal "jetty:run":

- build-type/dev.properties
- src/main/webapp/WEB-INF/jetty-env.xml
- src/main/webapp/WEB-INF/jetty/webdefault.xml

Also helpful:
- src/main/resources/log4j.xml for debug output

