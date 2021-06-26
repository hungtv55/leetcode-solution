const Result = { "win": 1, "loss": 2, "tie": 3 }

const cardOrder = ['A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2'];

const suites = ['C', 'D', 'H', 'S'];

function PokerHand(hand) {
    let cardInputs = hand.split(" ");
    this.cards = sortCards(cardInputs.map(input => new Card(input)));
    console.log(this.cards);
}

function Card(input) {
    this.value = input[0];
    this.suite = input[1];
    this.valueIndex = cardOrder.indexOf(this.value);
    this.suiteIndex = suites.indexOf(this.suite);
}

Card.prototype.compareTo = function(card) {
    if (this.valueIndex === card.valueIndex) {
        return 0;
    }
    return this.valueIndex < card.valueIndex ? 1 : -1;
}

function countByValue(hand) {
    const result = {};
    for (let card of hand.cards) {
        if (!result[card.value]) {
            result[card.value] = 1;
        } else {
            result[card.value] = result[card.value] + 1;
        }
    }
    return result;
}

function hasNumberOfValue(countMap, count) {
    const matchItem = Object.values(countMap).find(c => c === count);
    return !!matchItem;
}

function sortCards(cards) {
    return cards.sort((c1, c2) => cardOrder.indexOf(c1.value) > cardOrder.indexOf(c2.value));
}

function countSuits(hand) {
    const suites = [...new Set(hand.cards.map(c => c.suite))];
    return suites.length;
}

function hasFlush(hand) {
    return countSuits(hand) === 1;
}

function hasStraight(hand) {
    return hand.cards.every((card, index) => {
        if (index === 4) return true;
        return (card.valueIndex + 1 === hand.cards[index + 1].valueIndex);
    });
}

function hasThreeOfAKind(hand) {
    return hasAkind(3, hand);
}

function hasFullHouse(hand) {
   return hasAkind(3, hand) && hasAkind(2, hand);
}

function hasAkind(count, hand) {
    const countValueMap = countByValue(hand);
    return hasNumberOfValue(countValueMap, count);
}

function hasFourOfAKind(hand) {
    return hasAkind(4, hand);
}

function hasStraightFlush(hand) {
    if (hasFlush(hand)) {
        return hasStraight(hand);
    }
    return false;
}

function hasTwoPairs(hand) {
    const countMap = countByValue(hand);
    let firstPair = null;
    let secondPair = null;
    Object.keys(countMap).forEach((key) => {
        if (countMap[key] === 2) {
            if (!firstPair) {
                firstPair = key;
            } else {
                secondPair = key;
            }
        }
    });
    return firstPair && secondPair && firstPair !== secondPair;
}

function hasPair(hand) {
    return hasAkind(2, hand);
}

function highestCard(hand) {
    if (hasPair(hand)) {
        return false;
    }
    return true;
}

const bestHands = [
    hasStraightFlush, // 1
    hasFourOfAKind,   // 2
    hasFullHouse,     // 3
    hasFlush,         // 4
    hasStraight,      // 5
    hasThreeOfAKind,  // 6
    hasTwoPairs,      // 7
    hasPair,          // 8
    highestCard       // 9
  ];

PokerHand.prototype.compareWith = function(hand){
    console.log('hasStraightFlush:' + hasStraightFlush(this));
    console.log('hasStraightFlush:' + hasStraightFlush(hand));
    console.log('hasFourOfAKind:' + hasFourOfAKind(this));
    console.log('hasFourOfAKind:' + hasFourOfAKind(hand));
    let firstHandRank = 9;
    let secondHandRank = 9;
    for (let i = 0; i < bestHands.length; i++) {
        if(bestHands[i](this)) {
            firstHandRank = i;
            break;
        }
    }

    for (let i = 0; i < bestHands.length; i++) {
        if(bestHands[i](hand)) {
            secondHandRank = i;
            break;
        }
    }
    if (firstHandRank !== secondHandRank) {
        return firstHandRank < secondHandRank ? Result.win : Result.loss;
    }
    for (let i = 0; i < 5; i++) {
        if (this.cards[i].valueIndex < hand.cards[i].valueIndex) {
            return Result.win;
        }
        if (this.cards[i].valueIndex > hand.cards[i].valueIndex) {
            return Result.loss;
        }
    }
    return Result.tie;
}


const test = () => {
    console.log('---Straight flush---');
    const p = new PokerHand('2H 3H 4H 5H 6H');
  	const o = new PokerHand('KS AS TS QS JS');
    console.log(p.compareWith(o));
    console.log('---SF vs Four kind---');
    const p1 = new PokerHand('2H 3H 4H 5H 6H');
  	const o1 = new PokerHand('AS AD AC AH JD');
    p1.compareWith(o1);

    console.log('---Four kind ---');
    const p2 = new PokerHand('AS AH 2H AD AC');
  	const o2 = new PokerHand('JS JD JC JH 3D');
    p2.compareWith(o2);
    // p.hasStraight();
}

test();