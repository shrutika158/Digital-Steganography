from PIL import Image

def encode(img, message):
    full_message = message + "#"
    bytes_data = full_message.encode()
    msg_index = 0
    bit_index = 0

    img = img.convert("RGB")
    pixels = img.load()

    width, height = img.size

    for y in range(height):
        for x in range(width):
            if msg_index >= len(bytes_data):
                return img

            r, g, b = pixels[x, y]

            bit = (bytes_data[msg_index] >> (7 - bit_index)) & 1
            b = (b & 0xFE) | bit

            pixels[x, y] = (r, g, b)

            bit_index += 1
            if bit_index == 8:
                bit_index = 0
                msg_index += 1

    return img


def decode(img):
    img = img.convert("RGB")
    pixels = img.load()

    width, height = img.size

    message = ""
    current_byte = 0
    bit_count = 0

    for y in range(height):
        for x in range(width):
            r, g, b = pixels[x, y]

            current_byte = (current_byte << 1) | (b & 1)
            bit_count += 1

            if bit_count == 8:
                char = chr(current_byte)
                if char == "#":
                    return message
                message += char
                current_byte = 0
                bit_count = 0

    return message