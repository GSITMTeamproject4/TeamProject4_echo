document.addEventListener('DOMContentLoaded', function() {
    const quantityInput = document.getElementById('quantity');
    const minusBtn = document.querySelector('.quantity-btn.minus');
    const plusBtn = document.querySelector('.quantity-btn.plus');
    const totalPrice = document.querySelector('.total-price');
    const addToCartBtn = document.getElementById('addToCart');
    const addToWishlistBtn = document.getElementById('addToWishlist');

    const productPrice = parseFloat(totalPrice.textContent.replace(/[^0-9.-]+/g,""));

    function updateTotalPrice() {
        const quantity = parseInt(quantityInput.value);
        const total = productPrice * quantity;
        totalPrice.textContent = total.toLocaleString() + '원';
    }

    minusBtn.addEventListener('click', function() {
        if (quantityInput.value > 1) {
            quantityInput.value = parseInt(quantityInput.value) - 1;
            updateTotalPrice();
        }
    });

    plusBtn.addEventListener('click', function() {
        quantityInput.value = parseInt(quantityInput.value) + 1;
        updateTotalPrice();
    });

    quantityInput.addEventListener('change', updateTotalPrice);

    addToCartBtn.addEventListener('click', function() {
        // 장바구니 추가 로직
        alert('장바구니에 추가되었습니다.');
    });

    addToWishlistBtn.addEventListener('click', function() {
        // 찜하기 로직
        alert('찜 목록에 추가되었습니다.');
    });
});