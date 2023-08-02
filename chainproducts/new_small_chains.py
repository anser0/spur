import copy

# views ideals as a 2D array

# start = [[8, 8, 8, 8, 7, 6, 5, 4],
#          [8, 8, 8, 7, 6, 5, 4, 3],
#          [8, 8, 7, 6, 5, 4, 3, 2],
#          [8, 7, 6, 5, 4, 3, 2, 1],
#          [7, 6, 5, 4, 3, 2, 1, 0],
#          [6, 5, 4, 3, 2, 1, 0, 0],
#          [5, 4, 3, 2, 1, 0, 0, 0],
#          [4, 3, 2, 1, 0, 0, 0, 0]]

# start = [[6, 6, 6, 5, 4, 3], 
#          [6, 6, 5, 4, 3, 2], 
#          [6, 5, 4, 3, 2, 1], 
#          [5, 4, 3, 2, 1, 0], 
#          [4, 3, 2, 1, 0, 0], 
#          [3, 2, 1, 0, 0, 0]]

# start = [[4, 4, 3, 2],
#          [4, 3, 2, 1],
#          [3, 2, 1, 0],
#          [2, 1, 0, 0]]

# start = [[4, 4, 2, 2],
#          [4, 4, 2, 2],
#          [2, 2, 0, 0],
#          [2, 2, 0, 0]]

start = [[6, 6, 6, 3, 3, 3],
         [6, 6, 6, 3, 3, 3],
         [6, 6, 6, 3, 3, 3],
         [3, 3, 3, 0, 0, 0],
         [3, 3, 3, 0, 0, 0],
         [3, 3, 3, 0, 0, 0]]

n = len(start)

def check_potential_peak(ideal, i, j):
    return (j == n-1 or ideal[i][j] > ideal[i][j+1]) and (i == n-1 or ideal[i][j] > ideal[i+1][j]) and ideal[i][j] > 0

def get_potential_peaks(ideal):
    potential_peaks = set()
    for i in range(n):
        for j in range(n):
            if check_potential_peak(ideal, i, j):
                potential_peaks.add((i, j))
    return potential_peaks

def check_ideal(ideal):
    for i in range(n):
        for j in range(n-1):
            if ideal[i][j] < ideal[i][j+1]:
                return False
    for i in range(n-1):
        for j in range(n):
            if ideal[i][j] < ideal[i+1][j]:
                return False
    return True

def flip(ideal, coords):
    """
    ideal is a 2D array, coords is (i, j) of the point to be flipped
    """
    (i, j) = coords
    k = ideal[i][j] - 1 # need ideal[i][j] >= 1 to be able to flip
    new_ideal = copy.deepcopy(ideal)
    
    new_ideal[i][j] -= 1
    new_ideal[n - 1 - i][n - 1 - j] += 1
    
    if check_ideal(new_ideal):
        return new_ideal
    return ideal
    
def all_ideals():
    visited = [start]
    to_do = [start]

    while to_do:
        ideal = to_do.pop()
        
        for peak in get_potential_peaks(ideal):
            new_ideal = flip(ideal, peak)
            if check_ideal(new_ideal) and new_ideal not in visited:
                to_do.append(new_ideal)
                visited.append(new_ideal)
                
    return visited

##########

big_list = all_ideals() 

# print(len(big_list))

# starting_ideal = [[4, 4, 2, 2], [4, 4, 2, 2], [2, 2, 0, 0], [2, 2, 0, 0]]
starting_ideal = [[6, 6, 6, 3, 3, 3],
         [6, 6, 6, 3, 3, 3],
         [6, 6, 6, 3, 3, 3],
         [3, 3, 3, 0, 0, 0],
         [3, 3, 3, 0, 0, 0],
         [3, 3, 3, 0, 0, 0]]

for id in big_list:
    the_sum = 0
    for a in range(6):
        for b in range(6):
            the_sum += abs(starting_ideal[a][b] - id[a][b])
    if the_sum == 54:
        print(id)