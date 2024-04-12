db = db.getSiblingDB('test');

db.createCollection('Information');

db.Information.ensureIndex({"$**":"text"});