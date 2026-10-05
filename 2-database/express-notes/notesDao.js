const pool = require('./db');

async function getAll() {
  const result = await pool.query('SELECT id, text FROM note ORDER BY id');
  return result.rows;
}

async function getById(id) {
  const result = await pool.query('SELECT id, text FROM note WHERE id = $1', [id]);
  return result.rows[0];
}

async function create(text) {
  const result = await pool.query('INSERT INTO note (text) VALUES ($1) RETURNING id, text', [text]);
  return result.rows[0];
}

async function remove(id) {
  const result = await pool.query('DELETE FROM note WHERE id = $1', [id]);
  return result.rowCount > 0;
}

module.exports = { getAll, getById, create, remove };
