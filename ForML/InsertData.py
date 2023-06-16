
from datetime import datetime, timedelta
from google.cloud import storage
from google.oauth2 import service_account
import mysql.connector
import random

# Koneksi ke database
mydb = mysql.connector.connect(
    host="34.101.219.245",
    user="root",
    password="root",
    database="capstone_ayam"
)

# Membuat cursor
mycursor = mydb.cursor()

# Fungsi untuk menampilkan data result
def show_result_data():
    query = "SELECT * FROM data"
    mycursor.execute(query)
    result = mycursor.fetchall()
    
    for row in result:
        no, tanggal, jam, ayam, gambar, periode, harike, pakan, besok, pakan_besok = row
        pakan_formatted = "{:.2f}".format(pakan)  # Mengatur presisi desimal menjadi 2 digit
        pakan_besok_formatted = "{:.2f}".format(pakan_besok)  # Mengatur presisi desimal menjadi 2 digit
        print(f"No: {no}, Tanggal: {tanggal}, Jam: {jam}, Ayam: {ayam}, Gambar: {gambar}, Periode: {periode}, Harike: {harike}, Pakan: {pakan_formatted}, Besok: {besok}, Pakan Besok: {pakan_besok_formatted}")

# Fungsi untuk mengambil periode terakhir dari tabel periode
def get_last_periode():
    query = "SELECT periode FROM tb_periode ORDER BY no DESC LIMIT 1"
    mycursor.execute(query)
    result = mycursor.fetchone()
    if result:
        return result[0]
    else:
        return 0
    
# Fungsi untuk UPLOAD DATA KE STORAGE
def upload_image_to_cloud_storage(image_path, bucket_name, destination_blob_name):
    # Inisialisasi klien Cloud Storage dengan kredensial service account

    #TO DO ..(1) UBAH PATHNYA JADI LOKASI TEMPAT SIMPAN SERVICE ACCOUNTA
    storage_client = storage.Client(credentials=service_account.Credentials.from_service_account_file('D:\\UI\\Bangkit\\capstone\\ForML\\service-account-key.json'))

    # Mengunggah gambar ke Cloud Storage
    bucket = storage_client.bucket(bucket_name)
    blob = bucket.blob(destination_blob_name)
    blob.upload_from_filename(image_path)

    # Mengembalikan URL gambar yang diunggah
    url = blob.public_url
    return url


# Fungsi untuk mengambil tanggal mulai dari periode tertentu
def get_tanggal_mulai(periode):
    query = "SELECT tanggal_mulai FROM tb_periode WHERE periode = %s"
    values = (periode, )  # Mengubah menjadi list
    mycursor.execute(query, values)
    result = mycursor.fetchone()
    if result:
        return result[0]
    else:
        return None
# Mengisi tabel data dengan data dummy
def insert_dummy_data():
    tanggal_now = datetime.now().date()
    jam_now = datetime.now().time()
    sum_chicken = 42
    last_periode = get_last_periode()

    #TO DO ..(2) JADIKAN INPUT MANUAL
    ayam = int(sum_chicken) #gantikan dengan variabel data hasil anda

    #TO DO ..(3) INPUT GAMBAR 
    image_path = "D:\\UI\\Bangkit\\capstone\\ForML\\test.webp"  # Ganti dengan path gambar Anda

    
    bucket_name = "bucket-capstone-fix"  # Ganti dengan nama bucket Cloud Storage Anda
    destination_blob_name = "gambar.jpg"  # Ganti dengan nama file tujuan unggahan
    gambar = upload_image_to_cloud_storage(image_path, bucket_name, destination_blob_name)
    periode = last_periode
    harike = 0
    pakan = 0
    besok = 0
    pakan_besok = 0

    if last_periode > 0:
        tanggal_mulai = get_tanggal_mulai(last_periode)
        if tanggal_mulai:
            harike = (tanggal_now - tanggal_mulai).days
            besok = harike + 1

            # Mengambil nilai pakan hari ini
            query = "SELECT nilai FROM nilai WHERE harike = %s"
            values = (harike,)
            mycursor.execute(query, values)
            result = mycursor.fetchone()
            if result:
                pakan = ayam * result[0]

            # Mengambil nilai pakan besok
            query = "SELECT nilai FROM nilai WHERE harike = %s"
            values = (besok,)
            mycursor.execute(query, values)
            result = mycursor.fetchone()
            if result:
                pakan_besok = ayam * result[0]

    query = "INSERT INTO data (tanggal, jam, ayam, gambar, periode, harike, pakan, besok, pakan_besok) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)"
    values = (tanggal_now, jam_now, ayam, gambar, periode, harike, pakan, besok, pakan_besok)

    try:
        mycursor.execute(query, values)
        mydb.commit()
        print("Data berhasil dimasukkan")
    except mysql.connector.Error as error:
        print("Error inserting data:", error)
        mydb.rollback()

# Memanggil fungsi untuk mengisi tabel data dengan data dummy
insert_dummy_data()

#show_result_data()
#print (get_last_periode())

# Menutup koneksi database
mydb.close()
