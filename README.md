# MightyWidgets REST API
Welcome to the Mighty Widgets REST API reference. You can use this API to work with widget-type objects in your application. Mighty widgets provides an API to create, delete, and get widget objects in the format of JSON.
In the sections below, you can find documentation, expected HTTP response codes and sample requests to each available API method.
## URI structure
MightyWidgets REST API provides access to resources (data entities) via URI paths. To use a REST API, just make an HTTP request and parse the response in your application. 
The MightyWidgets REST API uses standard HTTP methods like GET, PUT, POST and DELETE, and JSON format. URIs for Jira's REST API resource have the following structure:
```
http://host:port/rest/api-version/resource-name
```
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
page | page number to get content from
size | number of elements to display on one page

example uri:
```
http://host:port/rest/api-version/resource-name/?page=1&size=50
```
this request returns page 1 with size of 50 elements
## Sorting
Sorting request is provided by "sort" parameter. You need to specify field name on which to sort by and you can add sorting direction ("asc" or "desc"). By default results are sorted in ascending direction by id field.
example uri:
```
http://host:port/rest/api-version/resource-name/?sort=X,desc
```
returned results will be sorted by "X" field in descending direction
## Resources
### Widgets
Provides REST access to Widgets
#### Find all
A request to get all the widgets, or widgets from specific canvas area. Supports pagination and sorting.
uri params:
Param | Description | Constraint
------------- | ------------- | ------------- 
x | x coordinate of canvas area to get widgets from | Not null, only 3 digits allowed
y | y coordinate of canvas area to get widgets from | Not null, only 3 digits allowed
width | width of canvas area to get widgets from | Not null, > 0, only 3 digits allowed
height | height of canvas area to get widgets from | Not null, > 0, only 3 digits allowed
page | page number to get content from |
size | number of elements to display on one page |
sort | sorting field name and direction
To get widgets from specific canvas area you must provide valid arguments for all canvas area parameters (x, y, width, height). 
returns JSON with paging information and widgets list.
example:
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
response codes
code | description
------------- | -------------
200 | response returned succesfully
404 | returned when requested page not found
500 | returned when sorting or paging information is invalid

#### Find by id
#### Add new or replace existing
#### Update existing
#### Delete exsisting

