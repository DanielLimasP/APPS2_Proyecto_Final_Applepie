const mongoose = require('mongoose')

const url = 'mongodb://localhost/paypal-qr-app'
mongoose.connect(url, {
    useCreateIndex: true,
    useNewUrlParser: true,
    useFindAndModify: false,
    useUnifiedTopology: true
}).then(db => console.log('DB connection established'))
.catch(err => console.error(err))