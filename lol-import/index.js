import { loadJsonFile } from "load-json-file";
import random, { Random } from "random";
import fs from 'fs'
import { escape } from "querystring";

const championsInfo = await loadJsonFile('champion.json')

const champions = championsInfo.data 
let sqlInserts = '';

for(const key in champions){
    const champion = champions[key]
    sqlInserts += generateInsert(champion)
}

try {
    fs.writeFileSync('champions.sql',sqlInserts)
} catch (err) {
    console.log(err);
}
function generateInsert(champion) {
    const {name,title,blurb} = champion
    const idMainRole = random.int(1,5)
    const insertSQL = `INSERT INTO champions(name, title, description, idMainRole) VALUES("${name}","${title}","${escape(blurb)}",${idMainRole});`
    return insertSQL
}

