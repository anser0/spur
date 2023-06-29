dim = 3

def all_ideals(l, w, h):
    """
    returns a list of all ideals
    """
    dimensions = [l, w, h]
    
    def get_children(point):
        """
        returns a set of the points directly covered by the given point in the chain poset
        """
        children = set()
        for i in range(dim):
            if point[i] > 0:
                children.add(point[:i] + (point[i]-1,) + point[i+1:])
            
        return children

    def flip(ideal, flip_peak):
        """
        returns a new ideal with the given peak flipped
        """
        
        reversed = tuple(dimensions[i] - 1 - flip_peak[i] for i in range(dim)) # reversed is guaranteed to be a peak (unless the peak is not actually a peak and peak is reversed's child)
        if flip_peak in get_children(reversed):
            return ideal
        
        new_ideal = ideal.union({reversed}).difference(get_children(reversed) | {flip_peak}) # peaks can disappear if they're covered by reverse
        
        to_add = get_children(flip_peak)
        for child in get_children(flip_peak): # peaks can be added if they're children of flip_peak and not covered by anything else
            # print("child:", child)
            for peak in new_ideal:
                i = 0
                while i < dim and peak[i] >= child[i]:
                    i += 1
                if i == dim: # peak covers child if peak[i] >= child[i] for all i
                    to_add = to_add.difference({child}) 
                    
        return new_ideal.union(to_add)
    
    def get_neighbors(starting_ideal):
        """
        returns a list of the neighbors
        """

        return [flip(starting_ideal, peak) for peak in starting_ideal]
    
    if h % 2 == 0:
        start = {(l-1, w-1, h//2 - 1)} # probably want h > 1
    elif w % 2 == 0:
        start = {(l-1, w//2 -1, h - 1)}
    else:
        start = {(l//2 -1, w-1, h - 1)}
    
    # use set of the peaks for visited list 
    visited = [start]
    to_do = [start]
    
    while to_do:
        ideal = to_do.pop()
        for neighbor in get_neighbors(ideal):
            if neighbor not in visited:
                to_do.append(neighbor)
                visited.append(neighbor)
    
    return visited

# for i in all_ideals(1, 2, 7):
#     print(i)
# print(len(all_ideals(1, 2, 7)))

for a in range(1, 10):
    for b in range(a, 10):
        for c in range(b, 10):
            if a*b*c % 2 == 0:
                print("dimensions:", a, "x", b, "x", c, ", number of vertices:", len(all_ideals(a, b, c)))
                # print(all_ideals(a, b, c))
