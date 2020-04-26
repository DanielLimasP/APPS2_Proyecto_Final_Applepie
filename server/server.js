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
var local = 'mongodb://localhost:paypal_qr_app'
MongoClient.connect(local, {useUnifiedTopology: true}, function(err, client){
  if(err){
    console.log("\nUnable to connect to the database server\n");
    console.log(err);
  }else{

    // Endpoint para el proceso de login y registro. SI el usuario ya esta registrado en la base de datos, 
    // entonces se le notifica que asi es... 
    // El nombre del endpoint solia ser 'signup' pero le cambiamos el nombre para que quedara acorde
    // a su nueva funcion...
    app.post('/login/', (request, response, next)=>{
      var post_data = request.body; //Get POST Params
      // -------> Codigo para generar contrasenas seguras
      //var plain_password = post_data.password;
      //var hash_data = saltHashPassword(plain_password);
      //var password = hash_data.passwordHash;
      //var salt = hash_data.salt;
      var name = post_data.name;
      var email = post_data.email;
      var google_id = post_data.google_id;
      var insertJson = {
        'email': email,
        //'password': password,
        //'salt': salt,
        'name': name,
        'id': google_id 
      };
      var db = client.db('paypal_qr_app');
      //función para checar si ya existe el email e ingresar datos nuevos
      db.collection('user')
        .find({'email':email}).count(function(err, number){
          if(number != 0){
              // Si el email ya existe, entonces notificar al usuario de que el login fue exitoso.
            response.json('Login exitoso');
            console.log('Login exitoso');
          }else{
            //Insertamos los datos ingresados
            db.collection('user')
              .insertOne(insertJson, function(error, res){
                response.json('Registro exitoso');
                console.log('Registro exitoso');
              });
          }
        });
    });

    //-------------------> Termina app.post('/register/') ---- endpoint para realizar el registro y el login
    // de los usuarios en nuestra base de docs
    //Comenzar el servidor web
    app.listen(3000, ()=>{
      console.log('------------> Conectado al servidor de MongoDB. Servidor escuchando en el puerto 3000');
    });
  }
});

