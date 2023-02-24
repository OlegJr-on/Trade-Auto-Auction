--------- CAR STATISTIC --------------

-- TOP 10 most popular Marks (by already sold) +
SELECT car.mark, count(od) as soldCount FROM order_details AS od
JOIN orders o on o.id = od.order_id
 JOIN bids b on b.id = o.bid_id
  JOIN lots l on l.id = b.lot_id
   JOIN cars car on car.id = l.car_id
WHERE od.order_status = 'PAID'
GROUP BY car.mark
ORDER BY soldCount desc
LIMIT (10);

-- TOP 10 marks which bring most income +
SELECT car.mark, sum(od.total_price) as income, count(od) FROM order_details AS od
JOIN orders o on o.id = od.order_id
 JOIN bids b on b.id = o.bid_id
  JOIN lots l on l.id = b.lot_id
   JOIN cars car on car.id = l.car_id
WHERE od.order_status = 'PAID'
GROUP BY car.mark
ORDER BY income desc
LIMIT (10);

-- List car by marketability +
SELECT car.mark, count(od) as soldCount FROM order_details AS od
JOIN orders o on o.id = od.order_id
 JOIN bids b on b.id = o.bid_id
  JOIN lots l on l.id = b.lot_id
   JOIN cars car on car.id = l.car_id
WHERE od.order_status = 'PAID'
GROUP BY car.mark
ORDER BY soldCount desc;

-- TOP 10 most popular mark (by makes bids) +
SELECT car.mark, count(b) as bidCount FROM bids AS b
  JOIN lots l on l.id = b.lot_id
   JOIN cars car on car.id = l.car_id
GROUP BY car.mark
ORDER BY bidCount desc
LIMIT (10);

-- TOP 10 marks by income from the commission +
SELECT car.mark, sum( od.total_price-b.bet ) as commission_income FROM order_details AS od
JOIN orders o on o.id = od.order_id
 JOIN bids b on b.id = o.bid_id
  JOIN lots l on l.id = b.lot_id
    JOIN cars car on car.id = l.car_id
WHERE od.order_status = 'PAID'
GROUP BY car.mark
ORDER BY commission_income desc
LIMIT 10;