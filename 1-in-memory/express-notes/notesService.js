let nextId = 1;
const notes = [];

function getAll() {
  return notes;
}

function getById(id) {
  return notes.find((note) => note.id === id);
}

function create(text) {
  const note = { id: nextId++, text };
  notes.push(note);
  return note;
}

function remove(id) {
  const index = notes.findIndex((note) => note.id === id);
  if (index === -1) return false;
  notes.splice(index, 1);
  return true;
}

module.exports = { getAll, getById, create, remove };
