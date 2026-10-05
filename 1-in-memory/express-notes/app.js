const express = require('express');
const notesService = require('./notesService');

const app = express();
app.use(express.json());

app.get('/notes', (req, res) => {
  res.json(notesService.getAll());
});

app.get('/notes/:id', (req, res) => {
  const note = notesService.getById(Number(req.params.id));
  if (!note) return res.status(404).end();
  res.json(note);
});

app.post('/notes', (req, res) => {
  const note = notesService.create(req.body.text);
  res.status(201).json(note);
});

app.delete('/notes/:id', (req, res) => {
  const removed = notesService.remove(Number(req.params.id));
  res.status(removed ? 204 : 404).end();
});

app.listen(3000, () => console.log('Listening on http://localhost:3000'));
