-- CREATE DATABASES
CREATE DATABASE `master0` CHARACTER SET utf8 COLLATE utf8_general_ci;
CREATE DATABASE `master0slave0` CHARACTER SET utf8 COLLATE utf8_general_ci;
CREATE DATABASE `master0slave1` CHARACTER SET utf8 COLLATE utf8_general_ci;
CREATE DATABASE `master1` CHARACTER SET utf8 COLLATE utf8_general_ci;
CREATE DATABASE `master1slave0` CHARACTER SET utf8 COLLATE utf8_general_ci;
CREATE DATABASE `master1slave1` CHARACTER SET utf8 COLLATE utf8_general_ci;
CREATE DATABASE `saga` CHARACTER SET utf8 COLLATE utf8_general_ci;

-- CREATE TABLE t_order
use master0;
CREATE TABLE `t_order_0` (
  `order_id` varchar(40) NOT NULL,
  `user_id` int(11) NOT NULL,
  `description` varchar(255) NOT NULL DEFAULT 'master0.t_order_0',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `t_order_1` (
  `order_id` varchar(40) NOT NULL,
  `user_id` int(11) NOT NULL,
  `description` varchar(255) NOT NULL DEFAULT 'master0.t_order_1',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `t_user_0` (
  `user_id` int(11) NOT NULL,
  `user_code` int(11) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL DEFAULT 'master0.t_user_0',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `t_user_1` (
  `user_id` int(11) NOT NULL,
  `user_code` int(11) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL DEFAULT 'master0.t_user_1',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

use master0slave0;
CREATE TABLE `t_order_0` (
  `order_id` varchar(40) NOT NULL,
  `user_id` int(11) NOT NULL,
  `description` varchar(255) NOT NULL DEFAULT 'master0slave0.t_order_0',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `t_order_1` (
  `order_id` varchar(40) NOT NULL,
  `user_id` int(11) NOT NULL,
  `description` varchar(255) NOT NULL DEFAULT 'master0slave0.t_order_1',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

use master0slave1;
CREATE TABLE `t_order_0` (
  `order_id` varchar(40) NOT NULL,
  `user_id` int(11) NOT NULL,
  `description` varchar(255) NOT NULL DEFAULT 'master0slave1.t_order_0',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `t_order_1` (
  `order_id` varchar(40) NOT NULL,
  `user_id` int(11) NOT NULL,
  `description` varchar(255) NOT NULL DEFAULT 'master0slave1.t_order_1',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

use master1;
CREATE TABLE `t_order_0` (
  `order_id` varchar(40) NOT NULL,
  `user_id` int(11) NOT NULL,
  `description` varchar(255) NOT NULL DEFAULT 'master1.t_order_0',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `t_order_1` (
  `order_id` varchar(40) NOT NULL,
  `user_id` int(11) NOT NULL,
  `description` varchar(255) NOT NULL DEFAULT 'master1.t_order_1',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `t_user_0` (
  `user_id` int(11) NOT NULL,
  `user_code` int(11) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL DEFAULT 'master1.t_user_0',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `t_user_1` (
  `user_id` int(11) NOT NULL,
  `user_code` int(11) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL DEFAULT 'master1.t_user_1',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

use master1slave0;
CREATE TABLE `t_order_0` (
  `order_id` varchar(40) NOT NULL,
  `user_id` int(11) NOT NULL,
  `description` varchar(255) NOT NULL DEFAULT 'master1slave0.t_order_0',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `t_order_1` (
  `order_id` varchar(40) NOT NULL,
  `user_id` int(11) NOT NULL,
  `description` varchar(255) NOT NULL DEFAULT 'master1slave0.t_order_1',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

use master1slave1;
CREATE TABLE `t_order_0` (
  `order_id` varchar(40) NOT NULL,
  `user_id` int(11) NOT NULL,
  `description` varchar(255) NOT NULL DEFAULT 'master1slave1.t_order_0',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `t_order_1` (
  `order_id` varchar(40) NOT NULL,
  `user_id` int(11) NOT NULL,
  `description` varchar(255) NOT NULL DEFAULT 'master1slave1.t_order_1',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;