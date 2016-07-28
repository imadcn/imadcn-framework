CREATE TABLE IF NOT EXISTS `t_order_0` (
  `orderId` varchar(50) NOT NULL,
  `userId` varchar(20) NOT NULL,
  PRIMARY KEY (`orderId`)
);

CREATE TABLE IF NOT EXISTS `t_order_item_0` (
  `itemId` varchar(50) NOT NULL,
  `orderId` varchar(50) NOT NULL,
  `userId` varchar(20) NOT NULL,
  PRIMARY KEY (`itemId`)
);

CREATE TABLE IF NOT EXISTS `t_order_1` (
  `orderId` varchar(50) NOT NULL,
  `userId` varchar(20) NOT NULL,
  PRIMARY KEY (`orderId`)
);

CREATE TABLE IF NOT EXISTS `t_order_item_1` (
  `itemId` varchar(50) NOT NULL,
  `orderId` varchar(50) NOT NULL,
  `userId` varchar(20) NOT NULL,
  PRIMARY KEY (`itemId`)
);