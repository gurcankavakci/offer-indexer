#OFFER : Ubuntu Technical Solution Search Engine

##Installation Instructors
1) In project path, run "docker-compose up -d" to start Solr.
2) Run Offer Indexer app main class to init collection and insert dummy document.
3) Run curl commands to config stemmer
```
curl -X POST -H 'Content-type:application/json' --data-binary '{
   "replace-field":{
   "name":"text",
   "type":"text_en",
   "stored":true }
   }' http://localhost:8983/solr/chats/schema
```

```
curl -X POST -H 'Content-type:application/json' --data-binary '{
   "replace-field":{
   "name":"question",
   "type":"text_en",
   "stored":true }
   }' http://localhost:8983/solr/chats/schema
```

4) Run Offer Indexer app main class again to reindex dummy document.
5) If CORS issue happens, download jetty-servlet-9.4.44 from https://mvnrepository.com/artifact/org.eclipse.jetty/jetty-servlets/9.4.44.v20210927
then copy image to solr container 
```
docker cp ./jetty-servlet-9.4.44.v20210927.jar 4a700:/opt/solr/server/solr-webapp/webapp/WEB-INF/lib
```
6)Edit the server/solr-webapp/webapp/WEB-INF/web.xml file to include the following filter right before the existing filter. On a clean Solr 5.0.0 download this entry will start at line 24.
```
<filter>
   <filter-name>cross-origin</filter-name>
   <filter-class>org.eclipse.jetty.servlets.CrossOriginFilter</filter-class>
   <init-param>
     <param-name>allowedOrigins</param-name>
     <param-value>*</param-value>
   </init-param>
   <init-param>
     <param-name>allowedMethods</param-name>
     <param-value>GET,POST,OPTIONS,DELETE,PUT,HEAD</param-value>
   </init-param>
   <init-param>
     <param-name>allowedHeaders</param-name>
     <param-value>origin, content-type, accept</param-value>
   </init-param>
 </filter>

 <filter-mapping>
   <filter-name>cross-origin</filter-name>
   <url-pattern>/*</url-pattern>
 </filter-mapping>
```
7) Restart Solr and test CORS issue
