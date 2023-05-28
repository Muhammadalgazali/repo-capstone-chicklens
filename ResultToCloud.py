import os
import pymysql
from google.cloud import storage
from datetime import datetime

# Konfigurasi Google Cloud Storage
os.environ['GOOGLE_APPLICATION_CREDENTIALS'] = 'D:\\Sebagai Tensor Flow\\bucket-olah.json'
storage_client = storage.Client()
bucket_name = 'bucket-capstone-algazali'

# Konfigurasi MySQL
db_host = '34.101.96.72'
db_user = 'root'
db_password = 'root'
db_name = 'latihan'

# Fungsi untuk mengupload gambar ke Google Cloud Storage
def upload_image_to_storage(image_path, image_name):
    bucket = storage_client.bucket(bucket_name)
    blob = bucket.blob(image_name)
    blob.upload_from_filename(image_path)
    print(f"Gambar {image_name} berhasil diupload ke Google Cloud Storage")

# Fungsi untuk menyimpan data ke basis data MySQL
def save_data_to_mysql(jam, tanggal, jumlah_ayam, jumlah_pakan, image_url):
    connection = pymysql.connect(host=db_host, user=db_user, password=db_password, database=db_name)
    cursor = connection.cursor()

    sql = "INSERT INTO ayam (jam, tanggal, jumlah_ayam, jumlah_pakan, gambar) VALUES (%s, %s, %s, %s, %s)"
    values = (jam, tanggal, jumlah_ayam, jumlah_pakan, image_url)

    try:
        cursor.execute(sql, values)
        connection.commit()
        print("Data berhasil disimpan ke MySQL")
    except:
        connection.rollback()
        print("Terjadi kesalahan saat menyimpan data ke MySQL")

    connection.close()

# Fungsi utama
def main(jumlah_ayam,jumlah_pakan,image_path):
    
    # Mendapatkan jam saat ini
    jam = datetime.now().strftime("%H:%M:%S")

    # Mendapatkan tanggal saat ini
    tanggal = datetime.now().strftime("%Y-%m-%d")
    
    # Nama gambar di Google Cloud Storage
    image_name = os.path.basename(image_path)
    
    # Upload gambar ke Google Cloud Storage
    upload_image_to_storage(image_path, image_name)
    
    # Dapatkan URL gambar dari Google Cloud Storage
    image_url = f"https://storage.googleapis.com/{bucket_name}/{image_name}"
    
    # Simpan data dan URL gambar ke MySQL
    save_data_to_mysql(jam, tanggal, jumlah_ayam, jumlah_pakan, image_url)

# Menjalankan program
if __name__ == '__main__':

    # TO DO ... (1)
    #inputan
    jumlah_ayam = input("Masukkan jumlah ayam: ")
    jumlah_pakan = input("Masukkan jumlah pakan: ")

    # TO DO ... (2)
    #Path gambar yang akan diupload
    image_path = 'D:/Sebagai Tensor Flow/IMG_4309.jpg'
    
    main(jumlah_ayam,jumlah_pakan,image_path)
