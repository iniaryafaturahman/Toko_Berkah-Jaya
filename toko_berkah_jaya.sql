-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 25, 2026 at 07:24 AM
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
-- Database: `toko_berkah_jaya`
--

-- --------------------------------------------------------

--
-- Table structure for table `tb_barang`
--

CREATE TABLE `tb_barang` (
  `id_barang` varchar(10) NOT NULL,
  `kategori` varchar(50) DEFAULT NULL,
  `nama_barang` varchar(100) DEFAULT NULL,
  `satuan` varchar(20) DEFAULT NULL,
  `harga_jual` double DEFAULT NULL,
  `stok` int(11) DEFAULT NULL,
  `kategori_text` varchar(50) DEFAULT NULL,
  `id_kategori` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `tb_barang`
--

INSERT INTO `tb_barang` (`id_barang`, `kategori`, `nama_barang`, `satuan`, `harga_jual`, `stok`, `kategori_text`, `id_kategori`) VALUES
('B001', 'Sembako', 'Beras Premium', 'kg', 15000, 33, NULL, 1),
('B002', 'Sembako', 'Gula Pasir', 'kg', 13500, 15, NULL, 1),
('B003', 'Makanan', 'Minyak Goreng', 'liter', 18000, 22, NULL, 1),
('B004', 'Makanan', 'Telur Ayam', 'butir', 3000, 88, NULL, 1),
('B005', 'Sabun', 'Sabun Cuci Piring', 'pcs', 5500, 40, NULL, 1),
('B006', 'Sembako', 'Telur Dinosaurus', 'kg', 120000, 76, NULL, 1),
('B007', 'Sembako', 'Genteng', 'pcs', 3000, 0, NULL, 1),
('B008', 'Makanan', 'Coklat', 'pcs', 7000, 14, NULL, 1);

-- --------------------------------------------------------

--
-- Table structure for table `tb_customer`
--

CREATE TABLE `tb_customer` (
  `id_customer` varchar(10) NOT NULL,
  `nama_customer` varchar(100) DEFAULT NULL,
  `alamat` text DEFAULT NULL,
  `telepon` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `tb_customer`
--

INSERT INTO `tb_customer` (`id_customer`, `nama_customer`, `alamat`, `telepon`) VALUES
('C001', 'Andi Wijaya', 'Jl. Merdeka No 10, Jakarta', '08123456789'),
('C002', 'Siti Aminah', 'Jl. Sudirman No 5, Bandung', '08129876543'),
('C003', 'Budi Santoso', 'Jl. Diponegoro No 12, Surabaya', '08134567773'),
('C004', 'Dewi Lestari', 'Jl. Gatot Subroto No 8, Medan', '08234567890'),
('C005', 'Asep Surasep', 'Jl. Unpam99', '08167726377'),
('C006', 'Ujang Gemini', 'Jl.Sangsekerta No 12, Khayangan', '08129983044'),
('C007', 'Arya', 'Jl.UNPAM', '0814555738');

-- --------------------------------------------------------

--
-- Table structure for table `tb_kategori`
--

CREATE TABLE `tb_kategori` (
  `id_kategori` int(11) NOT NULL,
  `nama_kategori` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `tb_kategori`
--

INSERT INTO `tb_kategori` (`id_kategori`, `nama_kategori`) VALUES
(6, 'Lainnya'),
(2, 'Makanan'),
(3, 'Minuman'),
(5, 'Rokok'),
(4, 'Sabun'),
(1, 'Sembako');

-- --------------------------------------------------------

--
-- Table structure for table `tb_penjualan`
--

CREATE TABLE `tb_penjualan` (
  `id_jual` int(11) NOT NULL,
  `tgl_transaksi` date DEFAULT NULL,
  `id_customer` varchar(10) DEFAULT NULL,
  `id_barang` varchar(10) DEFAULT NULL,
  `jumlah_beli` int(11) DEFAULT NULL,
  `total_bayar` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tb_transaksi_detail`
--

CREATE TABLE `tb_transaksi_detail` (
  `id_detail` int(11) NOT NULL,
  `id_transaksi` varchar(50) DEFAULT NULL,
  `id_barang` varchar(10) DEFAULT NULL,
  `jumlah_beli` int(11) DEFAULT NULL,
  `subtotal` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `tb_transaksi_detail`
--

INSERT INTO `tb_transaksi_detail` (`id_detail`, `id_transaksi`, `id_barang`, `jumlah_beli`, `subtotal`) VALUES
(1, 'TRX202605290836001FA1', 'B006', 5, 500000),
(2, 'TRX202605290836001FA1', 'B003', 12, 216000),
(3, 'TRX260604113956', 'B006', 12, 1440000),
(4, 'TRX260604113956', 'B003', 1, 18000),
(5, 'TRX260604113956', 'B002', 12, 162000),
(6, 'TRX260604113956', 'B001', 5, 75000),
(7, 'TRX260605080134', 'B002', 1, 13500),
(8, 'TRX260605080134', 'B003', 2, 36000),
(9, 'TRX260605080134', 'B004', 12, 36000),
(10, 'TRX260605080134', 'B007', 1000, 3000000),
(11, 'TRX260605082445', 'B001', 12, 180000),
(12, 'TRX260605082445', 'B002', 2, 27000),
(13, 'TRX260605082445', 'B006', 12, 1440000);

-- --------------------------------------------------------

--
-- Table structure for table `tb_transaksi_header`
--

CREATE TABLE `tb_transaksi_header` (
  `id_transaksi` varchar(50) NOT NULL,
  `tgl_transaksi` datetime DEFAULT NULL,
  `id_customer` varchar(10) DEFAULT NULL,
  `total_item` int(11) DEFAULT NULL,
  `total_bayar` double DEFAULT NULL,
  `metode_pembayaran` varchar(20) DEFAULT 'Cash'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `tb_transaksi_header`
--

INSERT INTO `tb_transaksi_header` (`id_transaksi`, `tgl_transaksi`, `id_customer`, `total_item`, `total_bayar`, `metode_pembayaran`) VALUES
('TRX202605290836001FA1', '2026-05-29 08:36:20', 'C003', 2, 716000, 'Cash'),
('TRX260604113956', '2026-06-04 11:40:58', 'C006', 4, 1695000, 'Cash'),
('TRX260605080134', '2026-06-05 08:02:26', 'C007', 4, 3085500, 'Cash'),
('TRX260605082445', '2026-06-05 08:25:29', 'C001', 3, 1647000, 'Cash');

-- --------------------------------------------------------

--
-- Table structure for table `tb_user`
--

CREATE TABLE `tb_user` (
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `nama_lengkap` varchar(100) DEFAULT NULL,
  `role` varchar(20) DEFAULT 'kasir'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `tb_user`
--

INSERT INTO `tb_user` (`username`, `password`, `nama_lengkap`, `role`) VALUES
('admin', 'admin123', 'Administrator', 'admin'),
('kasir1', 'kasir123', 'Kasir Satu', 'kasir'),
('Kasir2', 'kasir2', 'Kasir Dua', 'kasir');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tb_barang`
--
ALTER TABLE `tb_barang`
  ADD PRIMARY KEY (`id_barang`),
  ADD KEY `id_kategori` (`id_kategori`);

--
-- Indexes for table `tb_customer`
--
ALTER TABLE `tb_customer`
  ADD PRIMARY KEY (`id_customer`);

--
-- Indexes for table `tb_kategori`
--
ALTER TABLE `tb_kategori`
  ADD PRIMARY KEY (`id_kategori`),
  ADD UNIQUE KEY `nama_kategori` (`nama_kategori`);

--
-- Indexes for table `tb_penjualan`
--
ALTER TABLE `tb_penjualan`
  ADD PRIMARY KEY (`id_jual`),
  ADD KEY `id_customer` (`id_customer`),
  ADD KEY `id_barang` (`id_barang`);

--
-- Indexes for table `tb_transaksi_detail`
--
ALTER TABLE `tb_transaksi_detail`
  ADD PRIMARY KEY (`id_detail`),
  ADD KEY `id_barang` (`id_barang`),
  ADD KEY `fk_transaksi_detail_header` (`id_transaksi`);

--
-- Indexes for table `tb_transaksi_header`
--
ALTER TABLE `tb_transaksi_header`
  ADD PRIMARY KEY (`id_transaksi`),
  ADD KEY `id_customer` (`id_customer`);

--
-- Indexes for table `tb_user`
--
ALTER TABLE `tb_user`
  ADD PRIMARY KEY (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tb_kategori`
--
ALTER TABLE `tb_kategori`
  MODIFY `id_kategori` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `tb_penjualan`
--
ALTER TABLE `tb_penjualan`
  MODIFY `id_jual` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `tb_transaksi_detail`
--
ALTER TABLE `tb_transaksi_detail`
  MODIFY `id_detail` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `tb_barang`
--
ALTER TABLE `tb_barang`
  ADD CONSTRAINT `tb_barang_ibfk_1` FOREIGN KEY (`id_kategori`) REFERENCES `tb_kategori` (`id_kategori`);

--
-- Constraints for table `tb_penjualan`
--
ALTER TABLE `tb_penjualan`
  ADD CONSTRAINT `tb_penjualan_ibfk_1` FOREIGN KEY (`id_customer`) REFERENCES `tb_customer` (`id_customer`);

--
-- Constraints for table `tb_transaksi_detail`
--
ALTER TABLE `tb_transaksi_detail`
  ADD CONSTRAINT `fk_transaksi_detail_header` FOREIGN KEY (`id_transaksi`) REFERENCES `tb_transaksi_header` (`id_transaksi`) ON DELETE CASCADE;

--
-- Constraints for table `tb_transaksi_header`
--
ALTER TABLE `tb_transaksi_header`
  ADD CONSTRAINT `tb_transaksi_header_ibfk_1` FOREIGN KEY (`id_customer`) REFERENCES `tb_customer` (`id_customer`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
