const notesDao = require('./notesDao');

async function getAll() {
  return await notesDao.getAll();
}

async function getById(id) {
  return await notesDao.getById(id);
}

async function create(text) {
  return await notesDao.create(text);
}

async function remove(id) {
  return await notesDao.remove(id);
}

module.exports = { getAll, getById, create, remove };
