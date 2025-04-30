-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 30, 2025 at 06:44 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `tsp_game`
--

-- --------------------------------------------------------

--
-- Table structure for table `game_results`
--

CREATE TABLE `game_results` (
  `id` int(11) NOT NULL,
  `player_name` varchar(255) DEFAULT NULL,
  `home_city` varchar(1) DEFAULT NULL,
  `selected_cities` text DEFAULT NULL,
  `shortest_route` text DEFAULT NULL,
  `total_distance` double DEFAULT NULL,
  `total_time` double DEFAULT NULL,
  `algorithm_used` varchar(255) DEFAULT NULL,
  `timestamp` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `game_results`
--

INSERT INTO `game_results` (`id`, `player_name`, `home_city`, `selected_cities`, `shortest_route`, `total_distance`, `total_time`, `algorithm_used`, `timestamp`) VALUES
(18, 'jtgtyhtyh', 'G', '[D, H, I]', '[G, I, H, D, G]', 267, 0, 'Branch and Bound', '2025-04-27 17:10:28'),
(19, 'jtgtyhtyh', 'G', '[D, H, I]', '[G, I, H, D, G]', 267, 0, 'Branch and Bound', '2025-04-27 17:11:00'),
(22, 'hyyg', 'C', '[G]', '[]', 0, 0, 'Branch and Bound', '2025-04-27 17:19:19'),
(30, 'hyyg', 'C', '[G, B, J, I, A, D, F, H, E]', '[]', 0, 0, 'Branch and Bound', '2025-04-27 17:19:44'),
(37, 'hyyg', 'C', '[A, D, F, H, E]', '[C, D, F, A, E, H, C]', 393, 0, 'Nearest Neighbor', '2025-04-27 17:20:32'),
(38, 'hyyg', 'C', '[A, D, F, H, E, I, B]', '[C, D, F, A, E, H, I, B, C]', 527, 0, 'Nearest Neighbor', '2025-04-27 17:20:39'),
(39, 'ghjjjjjjjjjjj', 'D', '[B, E, A]', '[D, E, A, B, D]', 258, 0.007, 'Dijkstra', '2025-04-27 17:41:31'),
(40, 'gfffjgh', 'B', '[J, F, E]', '[B, J, F, E, B]', 268, 0, 'Nearest Neighbor', '2025-04-27 17:44:15'),
(41, 'yfuf', 'H', '[B, J]', '[H, B, J, H]', 207, 0, 'Nearest Neighbor', '2025-04-27 17:51:17'),
(53, 'suboda', 'J', '[I, F, C]', '[I, F, C, J]', 199, 0.001, 'Brute Force', '2025-04-28 05:16:19'),
(54, 'gtfgyt', 'C', '[B, H, D, E]', '[H, B, C, E, D]', 240, 0.004, 'Brute Force', '2025-04-28 05:17:11'),
(55, 'hyrgryt', 'C', '[G, I, F, H, D]', '[C, H, I, G, D, F]', 293, 0.008, 'Brute Force', '2025-04-28 06:33:40'),
(56, 'kkkkkk', 'E', '[B, J, C]', '[E, J, C, B]', 197, 0.001, 'Brute Force', '2025-04-28 06:39:12'),
(57, 'hrbfhjrttr', 'I', '[F, J, G, A]', '[I, C, A, A, E, A, A, J, A, A, G, A, A, H, A, A, B, A, A, F, A, A, D, A, I]', 1281, 0.004, 'Held-Karp', '2025-04-28 06:53:18'),
(58, 'dytuyu', 'F', '[C, J, D, A]', '[F]', 0, 0, 'Bellman-Ford', '2025-04-28 07:05:03'),
(60, 'dytuyu', 'F', '[C, J, D, A]', '[F]', 0, 0.001, 'Bellman-Ford', '2025-04-28 07:05:21'),
(66, 'dytuyu', 'F', '[C, D, A, H, I]', '[F]', 0, 0.001, 'Bellman-Ford', '2025-04-28 07:05:34'),
(67, 'dytuyu', 'F', '[C, D, A, I]', '[F]', 0, 0, 'Bellman-Ford', '2025-04-28 07:05:38'),
(71, 'hghy', 'H', '[J, E, F]', '[J, E, F, H, H]', 171, 0.002, 'Brute Force', '2025-04-28 07:18:26'),
(74, 'hhhhhhhhhh', 'C', '[F, B, I, H]', '[C, H, F, B, I, C]', 333, 0.027, 'Ant Colony Optimization', '2025-04-28 10:11:34'),
(75, 'Madhusha', 'H', '[D, F, C]', '[H, D, C, F, H]', 244, 0, 'Dijkstra Adapted', '2025-04-28 12:31:04'),
(76, 'Madhh', 'F', '[D, C, B, I]', '[F, D, C, B, I, F]', 345, 0.001, 'Dijkstra Adapted', '2025-04-28 12:59:50'),
(77, 'gjhkhjk', 'B', '[J, C, H]', '[B, J, C, H, B]', 264, 0, 'Dijkstra Adapted', '2025-04-28 13:05:40'),
(78, 'tgfyty', 'D', '[B, J, G]', '[D, G, B, J, D]', 260, 0.006, 'Simulated Annealing', '2025-04-28 13:12:55'),
(79, 'drgtrhyt', 'I', '[F, D, C]', '[I, C, D, F, I]', 289, 0.021, 'Ant Colony Optimization', '2025-04-28 13:20:00'),
(80, 'cccccccctbh', 'F', '[C, D, A]', '[F, C, A, D, F]', 268, 0.001, 'Dijkstra Adapted', '2025-04-28 13:22:03'),
(81, 'Madhusha', 'H', '[F, G, A, I, B]', '[H, A, B, I, F, G, H]', 377, 0.001, 'Dijkstra Adapted', '2025-04-28 13:28:30'),
(82, 'vvvvv', 'I', '[D, C, H]', '[I, H, D, C, I]', 250, 0.009, 'Simulated Annealing', '2025-04-28 13:29:18'),
(83, 'tftftftf', 'E', '[H, J, D]', '[E, J, D, H, E]', 229, 0.023, 'Ant Colony Optimization', '2025-04-28 13:30:06'),
(84, 'tftftftf', 'E', '[H, J, D]', '[E, J, D, H, E]', 229, 0.018, 'Ant Colony Optimization', '2025-04-28 13:30:37'),
(85, 'fghygj', 'A', '[C, G, D]', '[A, D, C, G, A]', 296, 0.022, 'Ant Colony Optimization', '2025-04-28 13:31:16'),
(86, 'ergt', 'A', '[D, B]', '[A, D, B, A]', 209, 0, 'Dijkstra Adapted', '2025-04-28 17:08:37'),
(87, 'ghjhjjj', 'F', '[G, J, I]', '[F, G, I, J, F]', 296, 0, 'Dijkstra Adapted', '2025-04-28 23:38:43'),
(88, 'gjghj', 'F', '[A, E, B]', '[F, A, B, E, F]', 269, 0.001, 'Dijkstra Adapted', '2025-04-29 01:48:15'),
(89, 'frtg', 'C', '[E, I, F, D]', '[C, I, F, D, E, C]', 330, 0.1, 'Dijkstra Adapted', '2025-04-29 01:50:21'),
(90, 'gfdhg', 'E', '[I, H]', '[E, I, H, E]', 228, 0, 'Dijkstra Adapted', '2025-04-29 02:05:30'),
(91, 'fdhfhyghj', 'D', '[F, J, G, C]', '[D, J, C, G, F, D]', 383, 0.0002, 'Dijkstra Adapted', '2025-04-29 02:12:26'),
(92, 'dhyhyjj', 'E', '[J, A, I, G, C]', '[E, G, C, J, A, I, E]', 379, 0.0001, 'Dijkstra Adapted', '2025-04-29 02:14:40'),
(93, 'dhyhyjj', 'E', '[J, A, I, G, C]', '[E, G, C, J, A, I, E]', 379, 0, 'Dijkstra Adapted', '2025-04-29 02:14:51'),
(96, 'dfhfgh', 'G', '[A, B, J, C, E]', '[G, A, E, J, C, B, G]', 404, 0.0002, 'Dijkstra Adapted', '2025-04-29 02:17:58'),
(97, 'trtyjyjuuu', 'E', '[D, C, G]', '[E, C, D, G, E]', 249, 0.0003, 'Dijkstra Adapted', '2025-04-29 02:18:43'),
(98, 'thytj', 'G', '[A, H, F]', '[G, F, A, H, G]', 309, 0.0001, 'Dijkstra Adapted', '2025-04-29 02:21:16'),
(99, 'ftytut', 'A', '[G, H, J]', '[A, J, G, H, A]', 303, 0.0002, 'Dijkstra Adapted', '2025-04-29 02:32:27'),
(100, 'yfyfy', 'F', '[I, B, H, J]', '[F, H, B, I, J, F]', 337, 0.0003, 'Dijkstra Adapted', '2025-04-29 03:36:57'),
(101, 'hjyyggyg', 'F', '[G, J, D, B]', '[F, J, G, D, B, F]', 332, 0.002, 'Dijkstra Adapted', '2025-04-29 03:41:04'),
(106, 'dthyt', 'H', '[C, A, E]', '[H, A, C, E, H]', 256, 0.001, 'Dijkstra Adapted', '2025-04-29 04:17:48'),
(107, 'jhbfgjdhgdjfhg', 'C', '[B, J, D, A, E]', '[C, D, A, J, B, E, C]', 451, 0.002, 'Dijkstra Adapted', '2025-04-29 04:19:00'),
(108, 'drytut', 'C', '[A, J, G, E, F, I, D, B]', '[C, J, A, I, G, F, D, E, B, C]', 544, 0.002, 'Dijkstra Adapted', '2025-04-29 04:19:31'),
(109, 'drthyt', 'D', '[I, C, J, H]', '[D, J, C, H, I, D]', 373, 0, 'Dijkstra Adapted', '2025-04-29 04:22:08'),
(144, 'jnrjr', 'D', '[C, E, G]', '[D, E, C, G, D]', 272, 0.001, 'Dijkstra Adapted', '2025-04-29 06:50:29'),
(145, 'duhfug', 'B', '[C, D, H]', '[B, D, H, C, B]', 262, 0.001, 'Dijkstra Adapted', '2025-04-29 07:58:06'),
(146, 'dfhgbhrt', 'D', '[E, B, I, J]', '[D, B, E, I, J, D]', 350, 0, 'Dijkstra Adapted', '2025-04-29 08:00:16'),
(147, 'regrty', 'J', '[A, G, I]', '[J, A, I, G, J]', 267, 0, 'Dijkstra Adapted', '2025-04-29 08:02:15'),
(148, 'sdfbhgtrh', 'C', '[C, F, G, E, I, J, D, H]', '[C, D, J, G, H, E, I, F, C]', 513, 0.001, 'Dijkstra Adapted', '2025-04-29 08:16:03'),
(149, 'tgtyujyu', 'D', '[C, D, H, B, F]', '[D, C, H, F, B, D]', 363, 0.001, 'Dijkstra Adapted', '2025-04-29 08:31:57'),
(150, 'sdfryegtrg', 'A', '[I, C, G]', '[A, I, C, G, A]', 254, 0, 'Dijkstra Adapted', '2025-04-29 08:33:39'),
(151, 'jenjrg', 'D', '[B, H, A, G]', '[D, A, G, B, H, D]', 388, 0, 'Dijkstra Adapted', '2025-04-30 02:46:19'),
(152, 'bugttth', 'F', '[A, E, H, C]', '[F, C, H, A, E, F]', 340, 0.009, 'Simulated Annealing', '2025-04-30 02:47:12'),
(153, 'jnjth', 'H', '[A, I, G]', '[H, A, I, G, H]', 264, 0.05, 'Ant Colony Optimization', '2025-04-30 02:47:34'),
(154, 'uhssugrt', 'G', '[B, F, E, A]', '[G, E, A, B, F, G]', 307, 0.009, 'Simulated Annealing', '2025-04-30 02:51:13'),
(155, 'gjuhju', 'I', '[H, J, E]', '[I, H, E, J, I]', 284, 0.029, 'Ant Colony Optimization', '2025-04-30 02:57:55'),
(156, 'hvyhy', 'B', '[A, C, I]', '[B, A, C, I, B]', 226, 0.028, 'Ant Colony Optimization', '2025-04-30 03:02:53'),
(157, 'jethuejrt', 'A', '[D, B, E, I, J]', '[A, E, I, D, B, J, A]', 363, 0.07, 'Ant Colony Optimization', '2025-04-30 03:13:30'),
(159, 'Lakshani', 'A', '[C, J, I, B, G]', '[A, J, B, C, I, G, A]', 397, 0.019, 'Simulated Annealing', '2025-04-30 07:21:21'),
(161, 'Madhusha', 'J', '[A, E, G, F, H, B]', '[J, H, A, E, B, F, G, J]', 459, 0.001, 'Dijkstra Adapted', '2025-04-30 07:32:29'),
(162, 'Nirmni', 'C', '[B, H, F, I, D]', '[C, H, I, F, B, D, C]', 434, 0, 'Dijkstra Adapted', '2025-04-30 07:35:07'),
(163, 'Sandaranga', 'E', '[J, H, G, A, F]', '[E, F, J, A, H, G, E]', 357, 0.01, 'Simulated Annealing', '2025-04-30 07:36:03'),
(164, 'Rashini', 'G', '[A, H, J, D, F]', '[G, A, D, H, J, F, G]', 385, 0.061, 'Ant Colony Optimization', '2025-04-30 07:37:00'),
(166, 'Malindi', 'G', '[C, I, A, E, D]', '[G, A, I, E, D, C, G]', 395, 0.001, 'Dijkstra Adapted', '2025-04-30 07:40:27'),
(167, 'Madhusha', 'C', '[G, I, B, J, D]', '[C, G, B, D, I, J, C]', 386, 0.006, 'Simulated Annealing', '2025-04-30 07:41:27'),
(168, 'Sachithra', 'D', '[H, I, B, C, A]', '[D, C, A, I, H, B, D]', 366, 0.055, 'Ant Colony Optimization', '2025-04-30 07:42:23'),
(169, 'abc', 'A', '[B, D, E, H]', '[A, H, E, D, B, A]', 307, 0, 'Dijkstra Adapted', '2025-04-30 08:59:25'),
(170, 'frtgtrth', 'H', '[D, G, I]', '[H, G, D, I, H]', 247, 0, 'Dijkstra Adapted', '2025-04-30 10:02:41');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `game_results`
--
ALTER TABLE `game_results`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `game_results`
--
ALTER TABLE `game_results`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=171;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
