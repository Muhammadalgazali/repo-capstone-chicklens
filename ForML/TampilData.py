from datetime import datetime
from google.cloud import storage
from google.oauth2 import service_account
import mysql.connector

# Koneksi ke database
mydb = mysql.connector.connect(
    host="34.101.219.245",
    user="root",
    password="root",
    database="capstone_ayam"
)

# Membuat cursor
mycursor = mydb.cursor()

# Fungsi untuk menampilkan data dari tabel data
def show_data():
    query = "SELECT * FROM data"
    mycursor.execute(query)
    result = mycursor.fetchall()

    for row in result:
        no, tanggal, jam, ayam, gambar, periode, harike, pakan, besok, pakan_besok = row
        pakan_formatted = "{:.2f}".format(pakan)  # Mengatur presisi desimal menjadi 2 digit
        pakan_besok_formatted = "{:.2f}".format(pakan_besok)  # Mengatur presisi desimal menjadi 2 digit
        print(f"No: {no}, Tanggal: {tanggal}, Jam: {jam}, Ayam: {ayam}, Gambar: {gambar}, Periode: {periode}, Harike: {harike}, Pakan: {pakan_formatted}, Besok: {besok}, Pakan Besok: {pakan_besok_formatted}")



# Fungsi untuk menampilkan data dari tabel tb_periode
def show_periode():
    query = "SELECT * FROM tb_periode"
    mycursor.execute(query)
    result = mycursor.fetchall()

    for row in result:
        no, periode, tanggal_mulai = row
        print(f"No: {no}, Periode: {periode}, Tanggal Mulai: {tanggal_mulai}")

# Fungsi untuk menampilkan data dari tabel nilai
def show_nilai():
    query = "SELECT * FROM nilai"
    mycursor.execute(query)
    result = mycursor.fetchall()

    for row in result:
        no, harike, nilai = row
        print(f"No: {no}, Harike: {harike}, Nilai: {nilai}")

        

# Main program
print("Menu:")
print("1. Tampilkan data dari tabel data")
print("2. Tampilkan periode peternakan")
print("3. Tampilkan jumlah pakan tiap hari dalam 35 hari")

choice = int(input("Masukkan pilihan (1/2/3): "))

if choice == 1:
    print("Data dari tabel data:")
    show_data()
elif choice == 2:
    print("Data periode peternakan:")
    show_periode()
elif choice == 3:
    print("Jumlah pakan tiap hari dalam 35 hari:")
    show_nilai()
else:
    print("Pilihan salah. Silakan jalankan kembali dan pilih opsi yang benar.")

# Menutup koneksi database
mydb.close()
