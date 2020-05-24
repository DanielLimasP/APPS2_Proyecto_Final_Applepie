const mongoose = require('mongoose')
const { Schema } = mongoose

const logSchema = new Schema({
    userEmail: {type: String, required: true},
    concept: {type: String, required: true},
    paypalme: {type: String, required: true},
    amount: {type: Number, required: true},
    date: {type: Date, required: true}
})

module.exports = mongoose.model('Log', logSchema)