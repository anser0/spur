"""
This program includes a function that verifies whether a point in [n]x[n]x[n] must be included
in all TSSC ideals (check_inequalities) and a function that returns all such points in a 2D array (must_include)
"""

def check_inequalities(i, j, k, n):
    """
    return True if the inequalities are satisfied (which makes the flip not work)
    """
    return ((2*i <= n-1 and j+k <= n-1) or (2*j <= n-1 and k+i <= n-1) or (2*k <= n-1 and i+j <= n-1))

def must_include(n):
    """
    n is the side length and is even
    """
    blank_array = [[0] * n for _ in range(n)]
    for i in range(n):
        for j in range(n):
            for k in range(n):
                if check_inequalities(i, j, k, n):
                    blank_array[i][j] += 1
                else:
                    break
    return blank_array
    
    