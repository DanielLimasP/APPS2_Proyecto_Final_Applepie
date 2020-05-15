/**
RESTFul services for PayPal_QR_App
Author: Daniel Limas
*/
var mongodb = require('mongodb');
var ObjectID = mongodb.ObjectID;
var crypto = require('crypto');
var express = require('express');
var bodyParser = require('body-parser');

//-------------------------------->
//Express HTTP heavy-lifting Mumbo-jumbo
var app = express();
app.use(bodyParser.json()); //JSON parameters 
app.use(bodyParser.urlencoded({extended: true})); //url parameters
//--------------------------------------->
//Connection to the mongoDB cluster and creation of the web server throught Express
var MongoClient = mongodb.MongoClient;
//var url = 'mongodb+srv://user1:Pys7OI28hsjfqx3i@cluster0-ezqai.mongodb.net/test?retryWrites=true&w=majority';
var local = 'mongodb://localhost:paypal-qr-app'
MongoClient.connect(local, {useUnifiedTopology: true}, function(err, client){
  if(err){
    console.log("\nUnable to connect to the database server\n");
    console.log(err);
  }else{
    app.post('/login/', (request, response, next)=>{
      var post_data = request.body; //Get POST Params
      var name = post_data.name;
      var email = post_data.email;
      var google_id = post_data.google_id;
      var insertJson = {
        'email': email,
        'name': name,
        'id': google_id 
      };
      var db = client.db('paypal-qr-app');
      db.collection('user')
        .find({'email':email}).count(function(err, number){
          if(number != 0){
            response.json('Login exitoso');
            console.log('Login exitoso');
          }else{
            db.collection('user')
              .insertOne(insertJson, function(error, res){
                response.json('Registro exitoso');
                console.log('Registro exitoso');
              });
          }
        });
    });

    //-------------------> app.post() ends
    app.listen(3000, ()=>{
      console.log('------------> DB Connected. Listening on Port 3000');
    });
  }
});

