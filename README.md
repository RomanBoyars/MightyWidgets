# MightyWidgets REST API
Welcome to the Mighty Widgets REST API reference. You can use this API to work with widget-type objects in your application. Mighty widgets provides an API to create, delete, and get widget objects in the format of JSON.

In the sections below, you can find documentation, expected HTTP response codes and sample requests to each available API method.
## URI structure
MightyWidgets REST API provides access to resources (data entities) via URI paths. To use a REST API, just make an HTTP request and parse the response in your application. 

The MightyWidgets REST API uses standard HTTP methods like GET, PUT, POST and DELETE, and JSON format. URIs for REST API resource have the following structure:
```
http://host:port/rest/api-version/resource-name
```

The default URI paths on local machine will be:

* Get All/Add new/Replace: http://localhost:8080/api/test/widgets/
* Find by ID/Delete: http://localhost:8080/api/test/widgets/{id}
* Update: http://localhost:8080/api/test/widgets/update/{id}

## Pagination
API uses pagination to limit response results, for resources that return large collections of items. Returned JSON wil contain paging metadata and a collection of items itself. Example:
```
{
        "page" : 0,
        "pageSize": 10,
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
Clients can use "page" and "size" parameters to get the desired set of results.

Param | Description
:------------ | :-------------
page | page number to get content from
size | number of elements to display on one page

example uri:
```
http://host:port/rest/api-version/resource-name/?page=1&size=50
```
this request returns page 1 with size of 50 elements
## Sorting
Sorting request is provided by "sort" parameter. You need to specify the field name on which to sort by and you can add sorting direction ("asc" or "desc"). By default results are sorted in ascending direction by id field.
example uri:
```
http://host:port/rest/api-version/resource-name/?sort=X,desc
```
returned results will be sorted by "X" field in descending direction
## Resources
### Widgets
Provides REST access to Widgets

### Find all. request method: GET
A request to get all the widgets, or widgets from specific canvas area. Supports pagination and sorting.

uri format:
>http://host:port/rest/api-version/widgets/

uri params:

Param | Description | Constraint
:------------- | :------------- | :------------- 
x | x coordinate of canvas area to get widgets from | Not null, only 3 digits allowed
y | y coordinate of canvas area to get widgets from | Not null, only 3 digits allowed
width | width of canvas area to get widgets from | Not null, > 0, only 3 digits allowed
height | height of canvas area to get widgets from | Not null, > 0, only 3 digits allowed
page | page number to get content from | Must be an existing page number ( < totalPages)
size | number of elements to display on one page | < 500
sort | sorting field name and direction | Must be an existing field name

To get widgets from specific canvas area you must provide valid arguments for all canvas area parameters (x, y, width, height). 
Returns JSON with paging information and widgets list.

Example:
```
   {
        "page" : 0,
        "pageSize": 10,
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
Response codes:

Code | Description
:------------- | :-------------
200 | response returned succesfully
500 | returned when sorting or paging information is invalid

### Find by id. request method: GET
Get a specific widget information by its ID.

uri format:
>http://host:port/rest/api-version/widgets/{id}

id must be number

returns JSON with information about requested widget. Example:

```
   {
        "id" : 0,
        "x" : 5,
        "y" : 10,
        "width" : 3,
        "height" : 3,
        "zIndex" : 5
    }
```

Response codes:

Code | Description
:------------- | :-------------
200 | response returned succesfully
404 | returned when the requested widget does not exist in repository
400 | returned when id is invalid

### Add new or replace existing. Request method: POST

Adds new widget to the repository, or replaces the existing.
Accepts JSON with widget information.

***Content-type: Application/Json***

Input JSON fields:

Field name | Description | Constraint | Additional info
:------------- | :------------- | :------------- | :------------- 
id | id of the widget | Must be a number and > 0 | If widget with provided ID already exists in reposetory, will replace it wit provided widget
x | x coordinate of canvas area to get widgets from | Not null, only 3 digits allowed | -
y | y coordinate of canvas area to get widgets from | Not null, only 3 digits allowed | -
width | width of canvas area to get widgets from | Not null, > 0, only 3 digits allowed | -
height | height of canvas area to get widgets from | Not null, > 0, only 3 digits allowed | -
zIndex | zIndex of the widget | >0 | If added zIndex already exists in repository, will increment all the indexes that are higher than provided

returns JSON with information about added/edited widget. Example:

```
   {
        "id" : 0,
        "x" : 5,
        "y" : 10,
        "width" : 3,
        "height" : 3,
        "zIndex" : 5
    }
```

Code | Description
:------------- | :-------------
200 | response returned succesfully
400 | returned when one or more request parameters is invalid, or JSON is malformed

### Update existing. Request method: PUT

Updates the existing widget in repository.
Accepts JSON with widget information.

uri format:
>http://host:port/rest/api-version/widgets/{id}

id must be a number

***Content-type: Application/Json***

Input JSON fields:

Field name | Description | Constraint | Additional info
:------------- | :------------- | :------------- | :------------- 
x | x coordinate of canvas area to get widgets from | Not null, only 3 digits allowed | -
y | y coordinate of canvas area to get widgets from | Not null, only 3 digits allowed | -
width | width of canvas area to get widgets from | Not null, > 0, only 3 digits allowed | -
height | height of canvas area to get widgets from | Not null, > 0, only 3 digits allowed | -
zIndex | zIndex of the widget | > 0 | If zIndex is changed and new zIndex already exists in repository, will increment all the indexes that are higher than provided

***id field in JSON will be ignored by this method***

returns JSON with information about updated widget. Example:

```
   {
        "id" : 0,
        "x" : 5,
        "y" : 10,
        "width" : 3,
        "height" : 3,
        "zIndex" : 5
    }
```

Code | Description
:------------- | :-------------
200 | response returned succesfully
404 | returned when the requested widget does not exist in repository
400 | returned when one or more request parameters is invalid, or JSON is malformed

### Delete exsisting. Request method: DELETE

Deletes a specific widget by its ID.

uri format:
>http://host:port/rest/api-version/widgets/{id}

id must be number

Returns a message that the widget was deleted successfully. Example:

```
Deleted widget 3
```

Response codes:

Code | Description
:------------- | :-------------
200 | response returned succesfully
404 | returned when the requested widget does not exist in repository
400 | returned when id is invalid
