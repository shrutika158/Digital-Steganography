import tkinter as tk
from tkinter import filedialog, messagebox
from PIL import Image
from steganography_processor import encode, decode

class SteganographyApp:
    def __init__(self, root):
        self.root = root
        self.root.title("Python Steganography App")
        self.root.geometry("600x400")

        self.image = None

        tk.Button(root, text="Load Image", command=self.load_image).pack(pady=5)

        tk.Label(root, text="Message:").pack()
        self.message_entry = tk.Entry(root, width=40)
        self.message_entry.pack(pady=5)

        tk.Button(root, text="Encode & Save", command=self.encode_image).pack(pady=5)
        tk.Button(root, text="Decode Message", command=self.decode_image).pack(pady=5)

    def load_image(self):
        file_path = filedialog.askopenfilename()
        if file_path:
            self.image = Image.open(file_path)
            messagebox.showinfo("Success", "Image Loaded Successfully!")

    def encode_image(self):
        if self.image is None:
            messagebox.showerror("Error", "Please load an image first.")
            return

        message = self.message_entry.get()
        if not message:
            messagebox.showerror("Error", "Message cannot be empty.")
            return

        encoded_image = encode(self.image, message)
        encoded_image.save("encoded_image.png")
        messagebox.showinfo("Success", "Message encoded and saved as encoded_image.png")

    def decode_image(self):
        if self.image is None:
            messagebox.showerror("Error", "Please load an image first.")
            return

        decoded_message = decode(self.image)
        messagebox.showinfo("Decoded Message", decoded_message)

if __name__ == "__main__":
    root = tk.Tk()
    app = SteganographyApp(root)
    root.mainloop()