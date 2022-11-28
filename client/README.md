# ALRR-Engineers Location Finder Client Applicaton

## Important note
	* The 2 clients developed for testing must already be in the database (for us they have client ids of 1 and 2)
	* The locations to the client must already be stored in the database as well (this simple app only retrieves data, you cannot add data from it)

## What is needed to run the client application
	* Need to have python 3 installed
	* Need to have flask installed (for this sample application, other deployers work as well)
	* Need to have a authentication token file in root directory of client
		* File needs to contain the encrypted string of the client's authentication token provided by the service
			* One can be added by reaching out to the developer of the service and they will add you to the database and provide a authentication token
			* Can encrypt the authentication token offline using the public key and convert into string format
				* Used RSA encryption scheme
				
## How to run the client
	* In the root directory of the client folder, run `python3 .\server.py`
	* This will start the client on the url 127.0.0.1:5000
		* The port number can be changed to any other
	* Go to the url the client is deployed on and you will be authenticated with the auth token file in the client root directory and if not authenticated it will say so
		* If not authenticated, you will be unable to get any results from the buttons on the page
	* If authenticated the client id to the authentication token will be returned and stored in the session for later use
	* There are 3 buttons on the page that will perform different requests to the service and pass back data
		* One button will retrieve all the location of the client associated with the authentication token and client id retrieved earlier
		* One button will retrieve all the claimed location of the client associated with the authentication token and client id retrieved earlier
		* One button will retrieve all the unclaimed location of the client associated with the authentication token and client id retrieved earlier
			
	
## Making other clients
	* Other clients can be created and can use all of the entrypoints specifed in the readme in the root of the repository as long as they have the necessary inputs for each entrypoint
	* Any other client using our service must first call the client/authenticate entrypoint to get their client id for the other entrypoints
	* Any client must encrypt their access token on their own using the public key for our service before sending it to us in any requests, otherwise they will not be able to access/modify their data