const express = require('express')
const router = express.Router()
const LogModel = require('../models/Log')

router.post('/uploadlog/', (req, res) => {
    let userEmail = req.body.userEmail
    let concept = req.body.concept
    let amount = req.body.amount
    let paypalme = req.body.paypalme
    let date = new Date()

    const newLog = new LogModel({userEmail, concept, amount, paypalme, date})

    newLog.save()
    res.json('Log created')
    console.log('Log created')
})

router.post('/getlogs/', (req, res) => {
    let logsConcepts = null
    LogModel.find({userEmail: req.body.queryparam}).exec()
    .then((concepts)=>{
        logsConcepts = concepts
        res.status(200).send({logsConcepts})
    }).catch((err)=>{
        res.status(500).send({ message: `Error in the request ${err}` })
    })
})

module.exports = router