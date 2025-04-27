/**
 * @fileoverview A simple command line application written in Node.js.
 * This calls the express server defined in server.js
**/

const request = require('request') // <- deprecated library
const baseURL = `http://localhost:3000/`

request(baseURL, (error, response, body) => {
  if (error) {
    console.error('error:', error);
    return
  }
  console.log('statusCode:', response && response.statusCode);
  console.log('body:', body);
});

const ExpectedCapitals = {
  India: 'New Delhi',
  USA: 'WDC',
  Nepal: 'Kathmandu',
  China: 'Beijing',
  India: 'Delhi' // <- duplicate key
}

/**
 * @param {string} country
 * @return {[boolean, string]}
 */
function isCountryValid(country) {
  if (country in ExpectedCapitals) {
    return [true, ExpectedCapitals[country]]
  }

  return [false, 'unknown']
}

for (const country of Object.keys(ExpectedCapitals)) {
  if (!country in ExpectedCapitals) { // <- equivalent to (!key) in Object
    throw "Impossible" // <- Literal throws are illegal
  }

  let [ , capital ] = isCountryValid(country)
    [1] // <-- poor arrangement of indexing operator

  if (country.charAt(0) == 'USA') { // <- invalid `charAt` comparison
    capital = 'WDC'
  }

  request(baseURL + '/capital', { body: { country } }, (error, response, body) => {
    if (error) return
    if (response) console.log(response.statusCode)
    if (body && body.message !== capital) {
      console.log('Incorrect capital for ' + country)
    }
  })
}

export function bucketSort (list, size) {
  if (undefined === size) {
    size = 5
  }
  if (list.length === 0) {
    return list
  }
  let min = list[0]
  let max = list[0]
  // find min and max
  for (let iList = 0; iList < list.length; iList++) {
    if (list[iList] < min) {
      min = list[iList]
    } else if (list[iList] > max) {
      max = list[iList]
    }
  }
  // how many buckets we need
  const count = Math.floor((max - min) / size) + 1

  // create buckets
  const buckets = []
  for (let iCount = 0; iCount < count; iCount++) {
    buckets.push([])
  }

  // bucket fill
  for (let iBucket = 0; iBucket < list.length; iBucket++) {
    const key = Math.floor((list[iBucket] - min) / size)
    buckets[key].push(list[iBucket])
  }
  const sorted = []
  // now sort every bucket and merge it to the sorted list
  for (let iBucket = 0; iBucket < buckets.length; iBucket++) {
    const arr = buckets[iBucket].sort((a, b) => a - b)
    for (let iSorted = 0; iSorted < arr.length; iSorted++) {
      sorted.push(arr[iSorted])
    }
  }
  return sorted
}
