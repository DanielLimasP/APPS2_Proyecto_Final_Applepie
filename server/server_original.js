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
app.use(bodyParser.json()); 
app.use(bodyParser.urlencoded({extended: true})); 
//--------------------------------------->
//Connection to the mongoDB cluster and creation of the web server throught Express
var MongoClient = mongodb.MongoClient;
var url = 'mongodb+srv://danyl:PAJLkkp8qBUutarD@cluster0-ezqai.mongodb.net/test?retryWrites=true&w=majority';
MongoClient.connect(url, {useUnifiedTopology: true}, function(err, client){
  if(err){
    console.log("\nUnable to connect to the database server\n");
    console.log(err);
  }else{
    app.post('/signup/', (request, response, next)=>{
      var post_data = request.body; // POST Params
      var name = post_data.name;
      var email = post_data.email;
      var google_id = post_data.google_id;
      var insertJson = {
        'email': email,
        'name': name,
        'id': google_id 
      };
      var db = client.db('paypal_qr_app');
      // If email exists then... else we add the data 
      db.collection('user')
        .find({'email':email}).count(function(err, number){
          if(number != 0){
            response.json('El email ingresado ya existe en la base de datos');
            console.log('Email ya existe en la base de datos');
          }else{
            db.collection('user')
              .insertOne(insertJson, function(error, res){
                response.json('Registro exitoso');
                console.log('Registro exitoso');
              });
          }
        });
    });
    //-------------------> app.post ends
    //-------------------> Login endpoint
    app.post('/login/', (request, response, next)=>{
      var post_data = request.body; // POST Params
      var email = post_data.email;
      var userPassword = post_data.password;

      var db = client.db('paypal_qr_app');
      db.collection('user')
        .find({'email':email}).count(function(error, number){
          if(number == 0){
            response.json('El email '+email+ ' ingresado no existe en la base de datos');
            console.log('El email '+email+' no existe en la base de datos');
          }else{
            db.collection('user')
              .findOne({'email': email}, function(error, user){
                if(hashed_password == encrypted_password){
                  response.json('Login exitoso para el usuario ' +user.name);
                  console.log('Login exitoso para el usuario ' +user.name);
                }else{
                  response.json('Te equivocaste en algo. Vuelve a intentar iniciar sesión');
                  console.log('Te equivocaste en algo. Vuelve a intentar iniciar sesión');
                }
              });
          }
        });
    });
    //-------------------> app.post ends
    app.listen(3000, ()=>{
      console.log('------------> DB connected. Listening on port 3000');
    });
  }
});
