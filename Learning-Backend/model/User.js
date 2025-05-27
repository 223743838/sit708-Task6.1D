const mongoose = require('mongoose');

const userSchema = new mongoose.Schema({
  username: String,
  email: String,
  password: String,
  phone: String,
  interests: [String],
  score: Number,
  premium: Boolean,
  history: [
    {
        task: String,
        score: Number,
        date: Date
    }
]
});



module.exports = mongoose.model('User', userSchema);
