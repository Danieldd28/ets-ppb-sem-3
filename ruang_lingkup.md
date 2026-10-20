# Soal 1: Deskripsi dan Ruang Lingkup Aplikasi LibraryReg

## 1. Deskripsi Aplikasi

Pendaftaran anggota di perpustakaan kampus masih pakai formulir kertas. Akibatnya antrean mengular di depan meja pendaftaran. Petugas sering salah catat atau menerima formulir yang isinya tidak lengkap. Mencari data satu anggota berarti membolak-balik tumpukan kertas, dan kartu anggota baru jadi setelah verifikasi dan pencetakan yang lama.

LibraryReg dibuat untuk menjawab masalah itu. Ini aplikasi Android, jadi calon anggota bisa mengisi formulir dari HP sendiri tanpa antre. Data langsung diperiksa sebelum disimpan. Petugas melihat semua anggota dalam satu daftar, mencari lewat nama atau nomor induk, membuka detailnya, lalu mengubah atau menghapus kalau perlu.

Aplikasinya ditulis dengan Kotlin dan Jetpack Compose, tampilannya Material Design 3. Datanya diatur lewat ViewModel dan StateFlow.

## 2. Pengguna

Ada dua jenis pengguna. Calon anggota butuh cara cepat untuk mendaftar tanpa kertas. Petugas perpustakaan butuh tempat untuk melihat, mencari, mengubah, dan menghapus data anggota.

## 3. Ruang Lingkup

### 3.1 Yang dikerjakan

Formulir pendaftaran berisi nama lengkap, NIM atau NIP, program studi, alamat, nomor telepon, email, dan jenis keanggotaan. Program studi dan jenis keanggotaan (Mahasiswa, Dosen, Staf) dipilih dari dropdown. Formulir yang sama dipakai lagi untuk edit, dengan isian yang sudah terisi dari data lama.

Validasi jalan saat tombol Daftar ditekan. Semua kolom wajib diisi. Email harus berformat email. Nomor induk hanya boleh angka sepanjang 8 sampai 18 digit, dan tidak boleh sama dengan anggota lain. Nomor telepon juga angka saja, 10 sampai 13 digit. Kalau ada yang salah, pesannya muncul tepat di bawah kolom itu dan data belum tersimpan. Tidak ada yang lolos begitu saja.

Daftar anggota tampil sebagai kartu, dengan jumlah total di atasnya. Kolom pencarian bekerja saat mengetik, mencocokkan nama atau nomor induk, dan ada tombol silang untuk menghapus kata kunci. Ketuk kartu untuk membuka halaman detail. Tombol edit dan hapus ada di tiap kartu, dan hapus selalu minta konfirmasi dulu. Kalau data kosong atau pencarian tidak menemukan apa-apa, layar menampilkan pesan khusus.

Sisanya hal kecil. Splash screen tampil 2 detik, dan ada 4 anggota contoh supaya layar tidak kosong waktu pertama dibuka.

### 3.2 Yang tidak dikerjakan

Data hanya disimpan di memori, jadi kembali ke data awal begitu aplikasi ditutup. Database belum dipakai karena materinya belum sampai sana. Login dan pembagian hak akses antara calon anggota dan petugas juga tidak ada, jadi siapa pun yang membuka aplikasi bisa melihat dan mengubah semuanya. Pencetakan kartu anggota, sinkronisasi ke server, peminjaman buku, denda, dan katalog buku juga di luar cakupan. Fokusnya memang pendaftaran.

### 3.3 Asumsi

Aplikasi dipakai di satu perangkat tanpa internet. Studi kasus hanya menulis "sesuai ketentuan" untuk nomor induk, jadi batas 8 sampai 18 digit dan 10 sampai 13 digit untuk telepon adalah keputusan kami sendiri. Daftar program studi tetap dan ditulis langsung di aplikasi.

## 4. Alur Navigasi

```
Splash -> (2 detik) -> Daftar Anggota
Daftar Anggota -> (tombol +)   -> Formulir Pendaftaran -> (Daftar / Batal) -> Daftar Anggota
Daftar Anggota -> (tap kartu)  -> Detail Anggota
Daftar Anggota -> (ikon edit)  -> Formulir Edit -> (Simpan / Batal) -> Daftar Anggota
Detail Anggota -> (ikon edit)  -> Formulir Edit
Detail Anggota -> (ikon hapus) -> Dialog Konfirmasi -> Daftar Anggota
```

## 5. Kebutuhan Non-Fungsional

Aplikasi jalan di Android 7.0 (API 24) ke atas. Tampilannya mengikuti Material Design 3 dan ikut berubah saat HP memakai mode gelap. Formulir bisa di-scroll, jadi tidak terpotong di layar kecil. Isian juga tidak hilang kalau layar diputar.
