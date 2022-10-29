module.exports = (sequelize, Sequelize) => {
  
  const Customer = sequelize.define("customer", {
    
    firstName: {
      type: Sequelize.STRING
    },
    
    
    lastName: {
      type: Sequelize.STRING
    },
    
    
    
    
    isDeleted: {
      type: Sequelize.BOOLEAN
    },
    
    
    insertedUserId: {
      type: Sequelize.Long
    },
    
    
    insertedDate: {
      type: Sequelize.DATE
    },
    
    
    updatedUserId: {
      type: Sequelize.Long
    },
    
    
    updatedDate: {
      type: Sequelize.DATE
    },
    
  });
  return Customer;
};
