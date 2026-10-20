import json
import requests
from PIL import Image
from io import BytesIO
import argparse
import sys

# 创建ArgumentParser对象
parser = argparse.ArgumentParser(description='Process some integers.')
# 添加参数
parser.add_argument('-json_file', type=str, help='Path to JSON file')
parser.add_argument('-image_url', type=str, help='Image URL')
# 解析命令行参数
args = parser.parse_args()

# 读取JSON文件内容
with open(args.json_file, 'r') as json_file:
    rect = json.load(json_file)  # 直接读取单个矩形对象

# 使用requests获取图片
response = requests.get(args.image_url)
image = Image.open(BytesIO(response.content))

# 从URL中提取图片名称，假设URL结尾是图片名称
original_image_name = args.image_url.split('/')[-1].split('.')[0]

# 根据矩形的左上角和右下角坐标，截取图片
right = rect['left'] + rect['width']
bottom = rect['top'] + rect['height']
cropped_image = image.crop((rect['left'], rect['top'], right, bottom))

# 将截取的图片保存到BytesIO对象中，并获取字节流
cropped_image_io = BytesIO()
cropped_image.save(cropped_image_io, format='PNG')
cropped_image_bytes = cropped_image_io.getvalue()

# 输出图片的字节流，前缀加上内容长度
print(f"{len(cropped_image_bytes)}", end='')
sys.stdout.buffer.write(cropped_image_bytes)
sys.stdout.flush()