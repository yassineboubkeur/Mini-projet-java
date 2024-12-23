-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 23, 2024 at 06:13 PM
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
-- Database: `jobs_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `job_offers`
--

CREATE TABLE `job_offers` (
  `id` int(11) NOT NULL,
  `job_title` varchar(255) DEFAULT NULL,
  `contract_type` varchar(255) DEFAULT NULL,
  `experience_required` varchar(255) DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `job_posts` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `job_offers`
--

INSERT INTO `job_offers` (`id`, `job_title`, `contract_type`, `experience_required`, `start_date`, `end_date`, `job_posts`) VALUES
(1, 'développeur php  casablanca maroc', 'cdi', 'De 1 à 3 ans', '2024-12-23', '2025-02-23', 5),
(2, 'bilingual team manager b2b engfr  rabat maroc', 'cdi', 'De 1 à 3 ans', NULL, NULL, 0),
(3, 'enseignant en architecture hf  rabat maroc', 'cdi', 'De 5 à 10 ans', '2024-12-23', '2025-02-23', 1),
(4, 'cloud engineer h f  rabat maroc', 'cdi', 'De 3 à 5 ans', '2024-12-23', '2025-01-14', 1),
(5, 'tech lead  cloud h f  rabat maroc', 'cdi', 'De 5 à 10 ans', '2024-12-23', '2025-01-14', 1),
(6, 'ingénieur travaux  génie civil anglophone  casablanca maroc', 'cdi', 'De 1 à 3 ans', '2024-12-23', '2025-02-23', 1),
(7, 'ingénieur cloud gcp senior  casablanca maroc', 'autre', 'De 5 à 10 ans', '2024-12-23', '2025-01-28', 2),
(8, 'spécialiste monitoring  tanger maroc', 'cdi', 'De 1 à 3 ans', '2024-12-23', '2025-02-23', 1),
(9, 'manager hf  agadir maroc', 'cdi', 'De 3 à 5 ans', '2024-12-23', '2025-02-23', 5),
(10, 'chargée de support informatique hf parfaitement bilingue  casablanca maroc', 'cdi', 'De 1 à 3 ans', '2024-12-23', '2025-02-23', 3);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `job_offers`
--
ALTER TABLE `job_offers`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `job_offers`
--
ALTER TABLE `job_offers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
