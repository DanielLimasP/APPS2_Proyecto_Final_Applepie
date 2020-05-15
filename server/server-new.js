/**
RESTFul services for PayPal_QR_App
Author: Daniel Limas
*/

const express = require('express')

var bodyParser = require('body-parser');

//Initializations
const app = express()
require('./database')

//Settings
app.set('port', process.env.PORT || 3000)

//Middlewares
app.use(express.urlencoded({extended:false}))
app.use(bodyParser.json()); //JSON parameters 
app.use(bodyParser.urlencoded({extended: true})); //url parameters

//Routes
app.use(require('./routes/users'))

//Server Initialize
app.listen(app.get('port'), ()=>{
    console.log('Server on port', app.get('port'))
})
