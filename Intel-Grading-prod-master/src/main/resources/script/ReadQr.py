import sys
import cv2
import numpy as np
import requests
from io import BytesIO
from pyzbar.pyzbar import decode


def decode_barcode(image_url):
    # 下载图片
    response = requests.get(image_url, verify=False)
    image = BytesIO(response.content)

    # 将图片从BytesIO对象中读取为OpenCV格式
    img = cv2.imdecode(np.asarray(bytearray(image.read()), dtype=np.uint8), cv2.IMREAD_COLOR)

    # 解码条形码
    decoded_objects = decode(img)
    for obj in decoded_objects:
        # 打印条形码数据和类型
        print(obj.data.decode('utf-8'))


if __name__ == "__main__":
    # 从命令行参数获取图片URL
    if len(sys.argv) > 1:
        image_url = sys.argv[1]
        decode_barcode(image_url)
    else:
        print("Usage: python barcode_decoder.py <image_url>")