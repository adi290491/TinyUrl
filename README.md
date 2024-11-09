# URL-SHORTENER SERVICE

## Project Overview


This is a Url-shortener service that well, shortens a long url into a 7 length short url. This is my solution for the Url shortener challenge ([https://codingchallenges.fyi/challenges/challenge-url-shortener](https://codingchallenges.fyi/challenges/challenge-url-shortener)) but I have developed this as a full fledged backend project. I have implemented it with Springboot with Redis and Postgres as my databases

## Features


* Url shortening uses MD5 hashing and requires jwt token to work  
* Includes user management service for register and login, provides jwt credentials essential for working with the shortening service  
* User service uses postgres to store user credentials which as encrypted.  
* Gateway service implemented with spring cloud which verifies jwt token before forwarding the request to url-shortener  
* Discovery server developed to register with Eureka to register the three services.  
* Both the url-shortener and user-service are load-balanced.

## TECHSTACK


* Java 21  
* Springboot 3.3.3  
* Redis  
* Postgres

## API ENDPOINTS


	1) Url-shortener  
   		a) For shortening 

		    curl -X POST http://localhost:8000/url-shortener/api/shorten  
		   -H “Authorization: Bearer jwt-token”  
		   -d ‘{  
		       "longUrl": "http://www.example.com"
		   }’  
		     
		   {  
		       "key": "abc1234",  
		       "longUrl": "http://www.example.com",  
		       "shortUrl": "http://localhost:8000/url-shortener/api/abc1234",  
		       "new": true (or false) 
		   }  
   	A newly creating short url should return with 204 Created, else 200 OK.  
   

		b) Redirect:  
			curl "http://localhost:8000/url-shortener/api/abc1234" 
			-H “Authorization: Bearer jwt-token”  
			HTTP/1.1 302 Found  
			Location: "http://www.example.com"
			content-length: 0
	
		c) Delete:  
			curl -X DELETE "http://localhost:8000/url-shortener/api/abc1234"
			-H “Authorization: Bearer jwt-token”  
			Response: true if delete successful, else false

	2) User Service    
		a) User Register: 
	   		curl -X POST http://localhost:8000/user-service/users/register  
	   		-d ‘{
			   "username": "test",
			   "email": "test@test.com",  
			   "Password": "test123"
			   }’

   		  	Response: 200 if successful  
     
		b) User Login
     			curl -X POST http://localhost:8000/user-service/users/login
			-d ‘{
			   "email": "test@test.com",
			   "password": "test123"
			}’

   		Response: 200 if successful, JWT token in header

		Feel free to adjust your configurations.

## Getting started

1) First start the discovery service  
2) Then start your core api services  
3) Then the gateway  
4) Also make sure to run the docker compose in url-shorter service to run redis  
5) Call and user register service and create credentials  
6) Invoke the user login service with the credentials to get the jwt token  
7) Pass the jwt token in Auth header when invoking the url-shortener services

   

