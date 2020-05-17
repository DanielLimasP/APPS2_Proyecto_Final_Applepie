const express = require('express')
const router = express.Router()
const UserModel = require('../models/User')

router.post('/login/', async (request, response)=>{
    var post_data = request.body; //Get POST Params
    var name = post_data.name;
    var email = post_data.email;
    var google_id = post_data.google_id;
    var paypalmeLink = post_data.paypalmeLink;
    const emailUser = await UserModel.findOne({email: email})
        if(emailUser){
            response.json('Successful login');
            console.log('Successful login');
        }else{
            const newUser = new UserModel({name, email, google_id, paypalmeLink})
            await newUser.save()
            response.json('Successful signup');
            console.log('Successful signup');
        }
  });

  module.exports = router