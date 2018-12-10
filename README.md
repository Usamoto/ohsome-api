# OHSOME API

This REST-based API aims to leverage the tools of the [OSHDB-API](https://github.com/GIScience/oshdb) through allowing to access some of its functionalities via HTTP requests.
The current stable beta-version is [v0.9.7](https://gitlab.gistools.geog.uni-heidelberg.de/giscience/big-data/ohsome/ohsome-api/tags/0.9.7).

[![](http://jenkins.ohsome.org/buildStatus/icon?job=ohsome-api/master)](http://jenkins.ohsome.org/blue/organizations/jenkins/ohsome-api/activity)

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) or higher
* [Apache Maven 3.5](https://maven.apache.org/download.cgi) or higher
* data: [download](http://downloads.ohsome.org/) it directly, or work through a guide on [how to prepare a new OSHDB extract](https://github.com/GIScience/oshdb/blob/master/oshdb-tool/etl/README.md)

### Setting-up/Running

1. checkout/download the repository
2. move to your Maven project directory in a shell (e.g. Windows PowerShell)
3. enter the command `mvn -DskipTests=true package` to build the project (if you want to build it running the integrated tests too, look at the section [Testing](https://gitlab.gistools.geog.uni-heidelberg.de/giscience/big-data/ohsome/ohsome-api#testing))
4. to run the jar file enter the following (if no additional keytables file is given, you can assume that it is included):
    * keytables included: `java -jar target/ohsome-api-0.9.7.jar --database.db=C:/path-to-your-data/ba-wue.oshdb`
    * keytables not included: `java -jar target/ohsome-api-0.9.7.jar --database.db=C:/path-to-your-data/ba-wue.oshdb --database.keytables=C:/path-to-your-keytablesFile/keytables`

Now you should have a running local API, which is ready for receiving requests under *http://localhost:8080/*.
<br>To check if it is running properly, you should be able to visit the swagger documentation under *http://localhost:8080/swagger-ui.html*.

*Note:*
* additionally you can add optional run-parameters:
    * to disable multithreading: `--database.multithreading=false`
    * to enable in-memory-caching: `--database.caching=true` (caution.. enabling this option requires quite some memory, but makes processing much faster)
* if you want to run the maven project in your IDE, you need to set the paths to your data in the run configurations
    * in Eclipse: *Run As --> Run Configurations --> (x)= Arguments --> Program arguments: 'enter the parameters here'*
* if you want to get information about the code directly, you can access the [Javadoc](https://docs.ohsome.org/java/ohsome-api/0.9.7/), which gets updated daily.

## Testing

To run the tests locally, you need the following:
1. define the properties `-Dport_get -Dport_post -Dport_data` using three free ports (for example 8081, 8082, 8083), which the API will use to start instances and run different integration tests on
    * -Dport.get starts data-aggregation + metadata tests using GET requests
    * -Dport.post starts data-aggregation tests using POST requests
    * -Dport.data starts data-extraction tests using GET and POST requests
2. [heidelberg.oshb](http://downloads.ohsome.org/v0.4/heidelberg.oshdb.mv.db) file (or any other, which includes the data from Heidelberg)
3. maven command: `mvn -Dport_get=8081 -Dport_post=8082 -Dport_data=8083 -DdbFilePathProperty="--database.db=C:\\path-to-your-heidelberg-file\\heidelberg.oshdb" package`

*Note:* 
* You can disable the integration and/or junit tests via the following properties: `-Dintegration="no" -Djunit="no"`
* If you do not define the -Dport_xyz property, the corresponding test class will not be executed

<p>
To be able to test this API with your own requests, you can take a look at the description of the parameters and available resources given in the [Swagger2](http://localhost:8080/swagger-ui.html) documentation, which can be accessed while your local copy is running.
It lists all available resources and gives detailled information about the individual input parameters and JSON responses.

## Examples

This section gives you some example request URLs and shows the returned JSON responses.
For more examples, please look at the [Swagger2](http://localhost:8080/swagger-ui.html) documentation.

* http://localhost:8080/elements/length?bboxes=8.6128,49.3183,8.7294,49.4376&types=way&time=2010-01-01/2016-08-01/P2Y2M2D&keys=highway&values=residential&showMetadata=true
<p> 

```json
{
    "attribution": {
        "url": "https://ohsome.org/copyrights",
        "text": "© OpenStreetMap contributors"
    },
    "apiVersion": "0.9",
    "metadata": {
        "executionTime": 858,
        "description": "Total length of lines in meter.",
        "requestUrl": "http://localhost:8080/elements/length?bboxes=8.6128,49.3183,8.7294,49.4376&types=way&time=2010-01-01/2016-08-01/P2Y2M2D&keys=highway&values=residential&showMetadata=true"
    },
    "result": [
        {
            "timestamp": "2010-01-01T00:00:00Z",
            "value": 344220.86
        },
        {
            "timestamp": "2012-03-03T00:00:00Z",
            "value": 352116.48
        },
        {
            "timestamp": "2014-05-05T00:00:00Z",
            "value": 351579.81
        },
        {
            "timestamp": "2016-07-07T00:00:00Z",
            "value": 350577.72
        }
    ]
}
```
<p>
* http://localhost:8080/elements/count/groupBy/boundary?bpolys={"type":"FeatureCollection","features":[{"type":"Feature","properties":{"id":"Heidelberg"},"geometry":{"type":"Polygon","coordinates":[[[8.684692,49.442905],[8.613625,49.43688],[8.613968,49.366726],[8.699455,49.356216],[8.731728,49.40427],[8.684692,49.442905]]]}},{"type":"Feature","properties":{"id":"Weinheim"},"geometry":{"type":"Polygon","coordinates":[[[8.656197,49.571762],[8.611565,49.543034],[8.675766,49.516518],[8.698769,49.55751],[8.656197,49.571762]]]}}]}&types=way&time=2015-01/2017-01-01/P1Y&keys=building&showMetadata=true
<p>

```json
{
    "attribution": {
        "url": "https://ohsome.org/copyrights",
        "text": "© OpenStreetMap contributors"
    },
    "apiVersion": "0.9",
    "metadata": {
        "executionTime": 670,
        "description": "Total number of items aggregated on the boundary object.",
        "requestUrl": "http://localhost:8080/elements/count/groupBy/boundary?bpolys=%7B%22type%22:%22FeatureCollection%22,%22features%22:[%7B%22type%22:%22Feature%22,%22properties%22:%7B%22id%22:%22Heidelberg%22%7D,%22geometry%22:%7B%22type%22:%22Polygon%22,%22coordinates%22:[[[8.684692,49.442905],[8.613625,49.43688],[8.613968,49.366726],[8.699455,49.356216],[8.731728,49.40427],[8.684692,49.442905]]]%7D%7D,%7B%22type%22:%22Feature%22,%22properties%22:%7B%22id%22:%22Weinheim%22%7D,%22geometry%22:%7B%22type%22:%22Polygon%22,%22coordinates%22:[[[8.656197,49.571762],[8.611565,49.543034],[8.675766,49.516518],[8.698769,49.55751],[8.656197,49.571762]]]%7D%7D]%7D&types=way&time=2015-01/2017-01-01/P1Y&keys=building&showMetadata=true"
    },
    "groupByBoundaryResult": [
        {
            "groupByObject": "Heidelberg",
            "result": [
                {
                    "timestamp": "2015-01-01T00:00:00Z",
                    "value": 21914
                },
                {
                    "timestamp": "2016-01-01T00:00:00Z",
                    "value": 22584
                },
                {
                    "timestamp": "2017-01-01T00:00:00Z",
                    "value": 24815
                }
            ]
        },
        {
            "groupByObject": "Weinheim",
            "result": [
                {
                    "timestamp": "2015-01-01T00:00:00Z",
                    "value": 6968
                },
                {
                    "timestamp": "2016-01-01T00:00:00Z",
                    "value": 9634
                },
                {
                    "timestamp": "2017-01-01T00:00:00Z",
                    "value": 11728
                }
            ]
        }
    ]
}
```

## Built With

* [Eclipse](http://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/oxygen1a) - IDE
* [Spring Boot](https://projects.spring.io/spring-boot/) - Web framework
* [Maven](https://maven.apache.org/) - Dependency management and project building

## Tested With

* [Postman](https://www.getpostman.com/) - Software to test REST APIs (build and send HTTP requests and view the responses)

## Authors


## License


## Acknowledgments

