
package com.mycompany.pos;

// Scanner digunakan untuk membaca input dari pengguna melalui terminal.
import java.util.Scanner;

// ==================================================
// BAGIAN 1: CLASS PRODUK
// PILAR OOP: ABSTRACTION DAN ENCAPSULATION
// ==================================================
// CLASS ABSTRAK: Produk menjadi cetakan umum bagi semua produk.
// ENCAPSULATION: atribut nama dan harga disembunyikan dengan private.
abstract class Produk {
    private String nama;
    private int harga;

    // Constructor: mengisi nama dan harga saat objek Produk dibuat.
    public Produk(String nama, int harga) {
        this.nama = nama;
        this.harga = harga;
    }

    // Getter: mengambil nama tanpa mengakses atribut private secara langsung.
    public String getNama() {
        return nama;
    }

    // Getter: mengambil harga produk.
    public int getHarga() {
        return harga;
    }

    // Method abstrak: class turunan wajib menentukan kategori produknya.
    public abstract String getKategori();
}


// ==================================================
// BAGIAN 2: CLASS BARANG
// PILAR OOP: INHERITANCE DAN POLYMORPHISM
// ==================================================
// INHERITANCE: Barang mewarisi nama dan harga dari Produk.
class Barang extends Produk {
    private int stok;

    // Constructor Barang memanggil constructor Produk dengan super(...).
    public Barang(String nama, int harga, int stok) {
        super(nama, harga);
        this.stok = stok;
    }

    public int getStok() {
        return stok;
    }

    // Mengurangi stok setelah transaksi berhasil dibayar.
    public void kurangiStok(int jumlah) {
        stok -= jumlah;
    }

    // POLYMORPHISM: implementasi getKategori() khusus untuk Barang.
    @Override
    public String getKategori() {
        return "Barang";
    }
}


// ==================================================
// BAGIAN 3: CLASS TRANSAKSI
// PILAR OOP: ABSTRACTION DAN ENCAPSULATION
// ==================================================
// CLASS ABSTRAK: mendefinisikan hal umum yang dimiliki semua transaksi.
// ENCAPSULATION: subtotal disimpan sebagai atribut private.
abstract class Transaksi {
    private int subtotal;

    public Transaksi(int subtotal) {
        this.subtotal = subtotal;
    }

    public int getSubtotal() {
        return subtotal;
    }

    // Class turunan menentukan besar diskon masing-masing.
    public abstract int getDiskon();

    // Class turunan menentukan nama jenis transaksinya.
    public abstract String getJenisTransaksi();

    // Total pembayaran = subtotal dikurangi diskon.
    public int hitungTotal() {
        return getSubtotal() - getDiskon();
    }
}


// ==================================================
// BAGIAN 4: CLASS TRANSAKSI NORMAL
// PILAR OOP: INHERITANCE DAN POLYMORPHISM
// ==================================================
// INHERITANCE: transaksi normal mewarisi struktur dari Transaksi.
class TransaksiNormal extends Transaksi {

    public TransaksiNormal(int subtotal) {
        super(subtotal);
    }

    // POLYMORPHISM: transaksi normal tidak memberikan diskon.
    @Override
    public int getDiskon() {
        return 0;
    }

    @Override
    public String getJenisTransaksi() {
        return "Transaksi Normal";
    }
}


// ==================================================
// BAGIAN 5: CLASS TRANSAKSI DISKON
// PILAR OOP: INHERITANCE DAN POLYMORPHISM
// ==================================================
// INHERITANCE: transaksi diskon juga mewarisi class Transaksi.
class TransaksiDiskon extends Transaksi {

    public TransaksiDiskon(int subtotal) {
        super(subtotal);
    }

    // POLYMORPHISM: diskon dihitung 10% dari subtotal.
    @Override
    public int getDiskon() {
        return getSubtotal() / 10;
    }

    @Override
    public String getJenisTransaksi() {
        return "Transaksi Diskon 10%";
    }
}


// ==================================================
// BAGIAN 6: CLASS UTAMA POS
// ==================================================
// CLASS UTAMA: program dijalankan melalui method main() di class POS.
public class POS {

    // Objek Scanner untuk menerima input pengguna.
    static Scanner input = new Scanner(System.in);

    // ----------------------------------------------
    // DATA BARANG
    // ----------------------------------------------
    // Array berisi data barang awal: nama, harga, dan stok.
    static Barang[] daftarBarang = {
        new Barang("Buku Tulis", 5000, 50),
        new Barang("Pulpen", 3000, 100),
        new Barang("Pensil", 2000, 80),
        new Barang("Penghapus", 1500, 40),
        new Barang("Penggaris", 4000, 30)
    };

    // ----------------------------------------------
    // DATA LAPORAN
    // ----------------------------------------------
    // Variabel laporan untuk menghitung transaksi, pendapatan, dan diskon.
    static int jumlahTransaksi = 0;
    static int totalPendapatan = 0;
    static int totalDiskon = 0;

    // ----------------------------------------------
    // METHOD MENAMPILKAN BARANG
    // ----------------------------------------------
    // METHOD: menampilkan seluruh barang beserta harga dan stoknya.
    static void tampilkanBarang() {
        System.out.println("\n========== DAFTAR BARANG ==========");
        System.out.printf("%-5s %-18s %-12s %-8s%n",
                "Kode", "Nama", "Harga", "Stok");

        // Perulangan untuk menampilkan setiap barang dalam array.
        for (int i = 0; i < daftarBarang.length; i++) {
            System.out.printf("%-5d %-18s Rp%-10d %-8d%n",
                    i + 1,
                    daftarBarang[i].getNama(),
                    daftarBarang[i].getHarga(),
                    daftarBarang[i].getStok());
        }
        System.out.println("===================================");
    }

    // ----------------------------------------------
    // METHOD TRANSAKSI PENJUALAN
    // ----------------------------------------------
    // METHOD TRANSAKSI: memilih barang, menghitung belanja, dan menerima pembayaran.
    static void transaksi() {
        // Menyimpan jumlah tiap barang yang dibeli pada transaksi ini.
        int[] jumlahBeli = new int[daftarBarang.length];
        int subtotal = 0;

        tampilkanBarang();

        // Mengulang pilihan barang sampai pengguna memasukkan kode 0.
        while (true) {
            System.out.print(
                    "\nMasukkan kode barang (0 untuk selesai): ");

            int kode = input.nextInt();

            // Kode 0 berarti pengguna selesai memilih barang.
            if (kode == 0) {
                break;
            }

            // Validasi: kode harus sesuai nomor barang yang tersedia.
            if (kode < 1 || kode > daftarBarang.length) {
                System.out.println("Kode barang tidak valid!");
                continue;
            }

            // Nomor kode dimulai dari 1, sedangkan indeks array dimulai dari 0.
            int indeks = kode - 1;
            Barang barang = daftarBarang[indeks];

            // Stok tersedia dikurangi jumlah yang sudah dipilih dalam transaksi ini.
            int stokTersedia = barang.getStok()
                    - jumlahBeli[indeks];

            if (stokTersedia <= 0) {
                System.out.println("Stok barang habis!");
                continue;
            }

            System.out.print("Masukkan jumlah beli: ");
            int jumlah = input.nextInt();

            if (jumlah <= 0) {
                System.out.println(
                        "Jumlah harus lebih dari 0!");
                continue;
            }

            // Mencegah pembelian melebihi stok yang tersedia.
            if (jumlah > stokTersedia) {
                System.out.println(
                        "Stok tidak cukup. Stok tersedia: "
                        + stokTersedia);
                continue;
            }

            // Catat jumlah pembelian dan tambahkan harga ke subtotal.
            jumlahBeli[indeks] += jumlah;
            subtotal += barang.getHarga() * jumlah;

            System.out.println("Barang berhasil ditambahkan.");
            System.out.println("Subtotal sementara: Rp" + subtotal);
        }

        // Jika tidak ada barang yang dibeli, transaksi dibatalkan.
        if (subtotal == 0) {
            System.out.println("Transaksi dibatalkan.");
            return;
        }

        // ------------------------------------------
        // MENENTUKAN JENIS TRANSAKSI
        // POLYMORPHISM
        // ------------------------------------------
        // POLYMORPHISM: satu variabel Transaksi dapat menunjuk dua class turunan.
        Transaksi transaksi;

        // Subtotal minimal Rp100.000 mendapat diskon 10%.
        if (subtotal >= 100000) {
            transaksi = new TransaksiDiskon(subtotal);
        } else {
            transaksi = new TransaksiNormal(subtotal);
        }

        // Memanggil perilaku sesuai objek transaksi yang dipilih.
        int diskon = transaksi.getDiskon();
        int totalBayar = transaksi.hitungTotal();

        // ------------------------------------------
        // MENAMPILKAN STRUK
        // ------------------------------------------
        System.out.println("\n============= STRUK BELANJA =============");

        // Menampilkan hanya barang yang benar-benar dibeli.
        for (int i = 0; i < daftarBarang.length; i++) {
            if (jumlahBeli[i] > 0) {
                int harga = daftarBarang[i].getHarga();
                int jumlah = jumlahBeli[i];

                System.out.println(daftarBarang[i].getNama());
                System.out.println(
                        jumlah + " x Rp" + harga
                        + " = Rp" + (jumlah * harga));
            }
        }

        System.out.println("-----------------------------------------");
        System.out.println("Jenis transaksi : "
                + transaksi.getJenisTransaksi());
        System.out.println("Subtotal        : Rp" + subtotal);
        System.out.println("Diskon          : Rp" + diskon);
        System.out.println("Total bayar     : Rp" + totalBayar);

        // ------------------------------------------
        // PEMBAYARAN
        // ------------------------------------------
        // Meminta pembayaran sampai uang mencukupi total belanja.
        int uangBayar;

        do {
            System.out.print("Uang pembayaran : Rp");
            uangBayar = input.nextInt();

            if (uangBayar < totalBayar) {
                System.out.println(
                        "Uang kurang Rp" + (totalBayar - uangBayar));
            }
        } while (uangBayar < totalBayar);

        // Kembalian adalah uang yang dibayar dikurangi total belanja.
        int kembalian = uangBayar - totalBayar;

        System.out.println("Kembalian       : Rp" + kembalian);
        System.out.println("Transaksi berhasil!");
        System.out.println("=========================================");

        // ------------------------------------------
        // MEMPERBARUI STOK DAN LAPORAN
        // ------------------------------------------
        // Setelah pembayaran berhasil, kurangi stok barang yang terjual.
        for (int i = 0; i < daftarBarang.length; i++) {
            if (jumlahBeli[i] > 0) {
                daftarBarang[i].kurangiStok(jumlahBeli[i]);
            }
        }

        jumlahTransaksi++;
        totalPendapatan += totalBayar;
        totalDiskon += diskon;
    }

    // ----------------------------------------------
    // METHOD LAPORAN PENJUALAN
    // ----------------------------------------------
    // METHOD LAPORAN: menampilkan ringkasan penjualan dan sisa stok.
    static void tampilkanLaporan() {
        System.out.println("\n========== LAPORAN PENJUALAN ==========");
        System.out.println("Jumlah transaksi : " + jumlahTransaksi);
        System.out.println("Total diskon     : Rp" + totalDiskon);
        System.out.println("Total pendapatan : Rp" + totalPendapatan);

        System.out.println("\n----------- SISA STOK BARANG -----------");

        for (int i = 0; i < daftarBarang.length; i++) {
            System.out.println(
                    daftarBarang[i].getNama()
                    + " : " + daftarBarang[i].getStok());
        }

        System.out.println("========================================");
    }

    // ----------------------------------------------
    // METHOD MAIN: PROGRAM UTAMA
    // ----------------------------------------------
    // METHOD MAIN: titik awal eksekusi program Java.
    public static void main(String[] args) {
        int pilihan;

        do {
            System.out.println("\n================================");
            System.out.println("       PROGRAM POS JAVA");
            System.out.println("================================");
            System.out.println("1. Lihat Daftar Barang");
            System.out.println("2. Transaksi Penjualan");
            System.out.println("3. Laporan Penjualan");
            System.out.println("4. Keluar");
            System.out.println("================================");
            System.out.print("Pilih menu: ");

            pilihan = input.nextInt();

            // Menjalankan menu sesuai angka yang dipilih pengguna.
            switch (pilihan) {
                case 1:
                    tampilkanBarang();
                    break;

                case 2:
                    transaksi();
                    break;

                case 3:
                    tampilkanLaporan();
                    break;

                case 4:
                    System.out.println(
                            "Terima kasih telah menggunakan POS.");
                    break;

                default:
                    System.out.println(
                            "Pilihan tidak valid!");
            }

        // Menu terus ditampilkan sampai pengguna memilih angka 4.
        } while (pilihan != 4);

        input.close();
    }
}
