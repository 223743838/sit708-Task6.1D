const mongoose = require('mongoose');

const taskSchema = new mongoose.Schema({
  userId: String,
  title: String,
  score: Number,
  total: Number,
  date: { type: Date, default: Date.now }
});

module.exports = mongoose.model('Task', taskSchema);
