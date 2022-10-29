const env = require('./env.js');

const Sequelize = require('sequelize');
const sequelize = new Sequelize(env.database, env.username, env.password, {
  host: env.host,
  dialect: env.dialect,
  operatorsAliases: false,

  pool: {
    max: env.max,
    min: env.pool.min,
    acquire: env.pool.acquire,
    idle: env.pool.idle
  }
});

const db = {};

db.Sequelize = Sequelize;
db.sequelize = sequelize;
db.customer = require('../models/customer.js')(sequelize, Sequelize);
db.contact = require('../models/contact.js')(sequelize, Sequelize);
db.email = require('../models/email.js')(sequelize, Sequelize);

db.address.belongsTo(db.customers, {foreignKey: 'fk_customerid', targetKey: 'uuid'});
db.customers.hasOne(db.address, {foreignKey: 'fk_customerid', targetKey: 'uuid'});

module.exports = db;