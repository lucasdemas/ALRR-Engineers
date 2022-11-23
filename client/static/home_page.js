function getAllLocations() {
    $.ajax({
        type: "GET",
        url: "http://locationfinder-env.eba-hg6rv8mb.us-east-1.elasticbeanstalk.com/location/getAll",
        success: function (result) {
          console.log(result);
          $("#getAllFill").empty();
          sessionStorage.setItem('test', 1);
          console.log(sessionStorage.getItem('test'));
          displayAllLocations(result);
        },
      });
}

function getAllClaimed() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/location/getClaim/claimed",
        success: function (result) {
          console.log(result);
          $("#getAllFill").empty();
          displayAllLocations(result);
        },
      });
}

function deleteClient() {
    console.log(sessionStorage.getItem('test'));
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/client/delete",
        data: {"clientId": 22},
        success: function (result) {
          console.log(result);
        },
      });
}


function displayAllLocations(locations) {
    if (locations.length == 0) {
        $("#getAllFill").text("No locations found");
    }
    $.each(locations, function(index, value) {
        console.log(value)
        let new_row=$("<div class='row'>")
        let new_lid=$("<div class='col-2'>")
        $(new_lid).html(value['id'])
        $(new_row).append(new_lid)
        let new_lcl=$("<div class='col-2'>")
        console.log(value['claim'].toString())
        if (value['claim'].toString() === 'true') {
            $(new_lcl).text('Claimed')
        }
        else {
            $(new_lcl).text('Unclaimed')
        }
        $(new_row).append(new_lcl)
        let new_la=$("<div class='col-2'>")
        $(new_la).html(value['area'])
        $(new_row).append(new_la)
        let new_lco=$("<div class='col-2'>")
        $(new_lco).html(value['cost'])
        $(new_row).append(new_lco)
        let new_ln=$("<div class='col-2'>")
        $(new_ln).html(value['name'])
        $(new_row).append(new_ln)
        let new_cid=$("<div class='col-2'>")
        $(new_cid).html(value['clientId'])
        $(new_row).append(new_cid)
        $("#getAllFill").append(new_row)
    })
}

function authorize_client(api_key) {
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/client/authenticate2",
        data: {"clientAuthToken": "iLc1/dBGmv/XtBQdhGEiR0tuAYesRHmt+2pHOLiq0wj7GtYdlq9bKfu9DwvbzrZJ/dY1E9wSQ4r7/sPe99zAQ1Yu+gniQtloMkSENEYGtMBPmje8kfnxatVjwbGnvbQ99UB5Xac4btJ/KeSNrCe+SBTRnJLNEwMEh3Zx+BWcOkJz8qfwAdN8L8/VgZpU5HxgoWPblobOlVRBBfBd/O/agGxCPUsO8ynYEro58qdK36oDNP/Cq+O5bEJI7W6oCzhDd+ZX3nOTmPumCyXKOeJypDMEiuW8ZN2fvLqN9kGRjKMSSc5Ru9Gcp2Adfnl97Rz36rR+MGlI0u0eF0AQJz6XZA=="},
        success: function (result) {
            console.log("Success");
            sessionStorage.setItem('client_id', result);
            console.log(sessionStorage.getItem('client_id'));
        },
        error: function(request, status, error){
            console.log("Error");
            console.log(request)
            console.log(status)
            console.log(error)
            alert(request.responseText);
        }
      });
}

function authorize_client2() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/client/authenticate2",
        data: {"auth_token": "auth_token1.txt"},
        success: function (result) {
            console.log("Success");
        },
        error: function(request, status, error){
            console.log("Error");
            console.log(request)
            console.log(status)
            console.log(error)
            alert(request.responseText);
        }
      });
}

$(document).ready(function() {
    console.log(api_key)
    authorize_client(api_key);

    $("#getAll").click(function() {
        getAllLocations();
    })

    $("#deleteClient").click(function() {
        deleteClient();
    })
})