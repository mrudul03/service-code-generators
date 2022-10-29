const env = {
  database: 'mscustomerdb',
  username: 'testusername',
  password: 'ChangeMe',
  host: 'localhost',
  dialect: 'mysql',
  pool: {
	  max: 5,
	  min: 0,
	  acquire: 30000,
	  idle: 10000
  }
};

module.exports = env;