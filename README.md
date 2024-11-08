# URL-SHORTENER SERVICE

## Project Overview

—

This is a Url-shortener service that well, shortens a long url into a 7 length short url. This is my solution for the Url shortener challenge ([https://codingchallenges.fyi/challenges/challenge-url-shortener](https://codingchallenges.fyi/challenges/challenge-url-shortener)) but I have developed this as a full fledged backend project. I have implemented it with Springboot with Redis and Postgres as my databases

## Features

—

* Url shortening uses MD5 hashing and requires jwt token to work  
* Includes user management service for register and login, provides jwt credentials essential for working with the shortening service  
* User service uses postgres to store user credentials which as encrypted.  
* Gateway service implemented with spring cloud which verifies jwt token before forwarding the request to url-shortener  
* Discovery server developed to register with Eureka to register the three services.  
* Both the url-shortener and user-service are load-balanced.

## TECHSTACK

—

* Java 21  
* Springboot 3.3.3  
* Redis  
* Postgres

## API ENDPOINTS

—

1) Url-shortener  
   a) For shortening 

    Endpoint: http://localhost:8000/url-shortener/api/shorten  
    Headers: Authorization: Bearer \<jwt-token\>  
    Method: POST  
    Request: {  
       "longUrl": \<your url\>  
   }  
   Response:   
   {  
       "key": "\<key\>",  
       "longUrl": \<long url\>,  
       "shortUrl": \<short url\>,  
       "new": \<flag for new/existing\>  
   }  
   A newly creating short url should return with 204 Created, else 200 OK.  
   

	b) Redirect:  
		Endpoint: \<short\_url\>  
		Headers: Authorization: Bearer \<jwt-token\>  
		Method: GET  
		Request: N/A  
		Response:  
		HTTP/1.1 302 Found  
Location: \<original url\>  
content-length: 0

	c) Delete:  
		Endpoint: \<short\_url\>  
		Headers: Authorization: Bearer \<jwt\_token\>  
		Method: DELETE  
		Request: N/A  
		Response: true if delete successful, else false

2) User Service    
1) User Register

                        Endpoint: http://localhost:8000/user-service/users/register  
		Method: POST  
		Headers: N/A  
		Request:

			{  
    "username": \<username\>,  
    "email": \<email\>,  
    "Password": \<pwd\>  
}  
		  Response:  
			200 if successful

2) User Login

     Endpoint: http://localhost:8000/user-service/users/login

		  Method: POST  
		  Headers: N/A  
		  Request:  
			{  
    "email": \<email\>,  
    "password": \<pwd\>  
}  
		  Response:  
			200 if successful  
			JWT token in header

Feel free to adjust your configurations.

## Getting started

—

1) First start the discovery service  
2) Then start your core api services  
3) Then the gateway  
4) Also make sure to run the docker compose in url-shorter service to run redis  
5) Call and user register service and create credentials  
6) Invoke the user login service with the credentials to get the jwt token  
7) Pass the jwt token in Auth header when invoking the url-shortener services

   

