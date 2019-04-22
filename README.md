# MightyWidgets REST API
Welcome to the Mighty Widgets REST API reference. You can use this API to work with widget-type objects in your application. Mighty widgets provides an API to create, delete, and get widget objects in the format of JSON.
In the sections below, you can find documentation, expected HTTP response codes and sample requests to each available API method.
## URI structure
MightyWidgets REST API provides access to resources (data entities) via URI paths. To use a REST API, just make an HTTP request and parse the response in your application. 
The MightyWidgets REST API uses standard HTTP methods like GET, PUT, POST and DELETE, and JSON format. URIs for Jira's REST API resource have the following structure:
>http://host:port/rest/api-version/resource-name
## Pagination
API uses pagination to limit response results, for resources that return large collections of items. Returned JSON wil contain paging metadata and a collection of items itself. Example:
```
{
        "page" : 0,
        "pageSize",
        "totalPages" : 10,
        "showing": 3,
        "totalElems": 3,
        "widgets": [
            { /* result 0 */ },
            { /* result 1 */ },
            { /* result 2 */ }
        ]
    }
```
Cliens can use "page" and "size" parameters to get the desired set of results.
Param | Description
------------ | -------------
Content from cell 1 | Content from cell 2
Content in the first column | Content in the second column
