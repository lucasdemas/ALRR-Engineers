function getAllLocations() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/location/getAll",
        data : {"clientId": sessionStorage.getItem('client_id'),
                "clientAuthToken": auth_key},
        success: function (result) {
          console.log(result);
          console.log(result.length);
          $("#getAllFill").empty();
          displayAllLocations(result);
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

function getAllClaimed() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/location/getClaim",
        data : {"isClaim": "claimed",
                "clientId": sessionStorage.getItem('client_id'),
                "clientAuthToken": auth_key},
        success: function (result) {
          console.log(result);
          $("#getAllFill").empty();
          displayAllLocations(result);
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

function getAllUnclaimed() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/location/getClaim",
        data : {"isClaim": "unclaimed",
                "clientId": sessionStorage.getItem('client_id'),
                "clientAuthToken": auth_key},
        success: function (result) {
          console.log(result);
          $("#getAllFill").empty();
          displayAllLocations(result);
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

function displayAllLocations(locations) {
    if (locations.length == 0) {
        $("#getAllFill").text("No locations found");
    } else {
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
}

function authorize_client(api_key) {
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/client/authenticate",
        data: {"clientAuthToken": auth_key},
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

$(document).ready(function() {
    console.log(auth_key)
    authorize_client(auth_key);

    $("#getAll").click(function() {
        getAllLocations();
    })

    $("#claimedLoc").click(function() {
        getAllClaimed();
    })

    $("#unclaimedLoc").click(function() {
        getAllUnclaimed();
    })
})