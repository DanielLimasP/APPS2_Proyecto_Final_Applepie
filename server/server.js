/**
RESTFul services for PayPal_QR_App
Author: Daniel Limas
*/
var mongodb = require('mongodb');
var ObjectID = mongodb.ObjectID;
var crypto = require('crypto');
var express = require('express');
var bodyParser = require('body-parser');
//------------------------------->
//Password encryption mumbo-jumbo
var genRandomString = function(length){
  return crypto.randomBytes(Math.ceil(length/2))
    .toString('hex') //Pasar a formato hex
    .slice(0, length); //regresar el número de caracteres requerido
};
var sha512 = function(password, salt){
  var hash = crypto.createHmac('sha512', salt);
  hash.update(password);
  var value = hash.digest('hex');
  return{
    salt: salt,
    passwordHash: value
  };
}
function saltHashPassword(userPassword){
  var salt = genRandomString(16);
  var passwordData = sha512(userPassword, salt);
  return passwordData;
}
function checkHashPassword(userPassword, salt){
  var passwordData = sha512(userPassword, salt);
  return passwordData;
}
//-------------------------------->
//Express HTTP heavy-lifting Mumbo-jumbo
var app = express();
app.use(bodyParser.json()); //Para aceptar parámetros en JSON
app.use(bodyParser.urlencoded({extended: true})); //Para aceptar parámetros en la url
//--------------------------------------->
//Connection to the mongoDB cluster and creation of the web server throught Express
var MongoClient = mongodb.MongoClient;
var url = 'mongodb+srv://danyl:PAJLkkp8qBUutarD@cluster0-ezqai.mongodb.net/test?retryWrites=true&w=majority';
MongoClient.connect(url, {useUnifiedTopology: true}, function(err, client){
  if(err){
    console.log("\nUnable to connect to the database server\n");
    console.log(err);
  }else{

    //Endpoint para el proceso de registro
    app.post('/signup/', (request, response, next)=>{
      var post_data = request.body; //Get POST Params
      var plain_password = post_data.password;
      var hash_data = saltHashPassword(plain_password);
      var password = hash_data.passwordHash;
      var salt = hash_data.salt;
      var name = post_data.name;
      var email = post_data.email;
      var insertJson = {
        'email': email,
        'password': password,
        'salt': salt,
        'name': name
      };
      var db = client.db('paypal_qr_app');
      //función para checar si ya existe el email e ingresar datos nuevos
      db.collection('user')
        .find({'email':email}).count(function(err, number){
          if(number != 0){
            response.json('El email ingresado ya existe en la base de datos');
            console.log('Email ya existe en la base de datos');
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
    //-------------------> Termina app.post('/register/') ---- endpoint para realizar el registro
    //Creamos un endpoint para el Login
    app.post('/login/', (request, response, next)=>{
      var post_data = request.body; //Get POST Params
      var email = post_data.email;
      var userPassword = post_data.password;

      var db = client.db('paypal_qr_app');
      //función para checar si ya existe el email e ingresar datos nuevos
      db.collection('user')
        .find({'email':email}).count(function(error, number){
          if(number == 0){
            response.json('El email '+email+ ' ingresado no existe en la base de datos');
            console.log('El email '+email+' no existe en la base de datos');
          }else{
            //Checamos los datos del usuario
            db.collection('user')
              .findOne({'email': email}, function(error, user){
                var salt = user.salt;
                var hashed_password = checkHashPassword(userPassword, salt).passwordHash;
                var encrypted_password = user.password;
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
    //-------------------> Termina app.post('/login/') ---- endpoint para realizar el login de los usuarios
    //Comenzar el servidor web
    app.listen(3000, ()=>{
      console.log('------------> Conectado al servidor de MongoDB. Servidor escuchando en el puerto 3000');
    });
  }
});
